package com.example.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.curriculum.SyllabusRepository
import com.example.data.local.AppDatabase
import com.example.data.local.LocalChapterProgressEntity
import com.example.data.local.OfflineNoteEntity
import com.example.data.pyq.PyqDiscoveryStatus
import com.example.data.pyq.PyqRepository
import com.example.data.remote.FirestoreRepository
import com.example.data.textbook.TextbookRepository
import com.example.model.ChapterProgress
import com.example.model.ExamAttempt
import com.example.model.OfficialTextbook
import com.example.model.SavedStudyNote
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.model.SyllabusChapter
import com.example.model.SyllabusVerificationInfo
import com.example.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CurriculumUiState(
    val selectedSubject: SubjectFilter = SubjectFilter.ALL,
    val selectedChapterId: String = SyllabusRepository.chapters.first().id,
    val selectedTopicId: String = SyllabusRepository.chapters.first().topics.first().id,
    val selectedLanguage: StudyLanguage = StudyLanguage.ENGLISH,
    val searchQuery: String = "",
    val recentChapterIds: List<String> = listOf("chem_reactions"),
    val dailyTargetMinutes: Int = 45,
    val verificationInfo: SyllabusVerificationInfo = SyllabusRepository.baselineVerification,
    val pyqDiscoveryStatus: PyqDiscoveryStatus = PyqDiscoveryStatus(
        lastSyncedLabel = "Local paper archive and practice bank",
        archiveIndexReachable = false,
        archiveCollectionsCount = PyqRepository.paperArchive.size,
        message = "${PyqRepository.paperArchive.size} paper archive entries cover 2020–2026 and historical/current model papers."
    ),
    val syncErrorMessage: String? = null,
    val statusBannerMessage: String? = null
) {
    val availableChapters: List<SyllabusChapter>
        get() = SyllabusRepository.searchChaptersAndTopics(searchQuery, selectedSubject)

    val selectedChapter: SyllabusChapter
        get() = SyllabusRepository.getChapterById(selectedChapterId)
}

class SharedCurriculumViewModel(
    appContext: Context,
    private val firestoreRepository: FirestoreRepository,
    private val currentUserId: String,
    private val displayName: String
) : ViewModel() {

    private val db = AppDatabase.getInstance(appContext)
    private val textbookRepository = TextbookRepository(appContext, db.textbookDownloadDao())
    private val localProgressDao = db.localProgressDao()
    private val offlineNoteDao = db.offlineNoteDao()
    private val localExamDao = db.localExamHistoryDao()

    private val _uiState = MutableStateFlow(CurriculumUiState())
    val uiState: StateFlow<CurriculumUiState> = _uiState.asStateFlow()

    val textbooks: StateFlow<List<OfficialTextbook>> = textbookRepository.observeTextbooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TextbookRepository.officialCatalogue
        )

    val cloudProfile: StateFlow<UserProfile?> = firestoreRepository.observeUserProfile(currentUserId)
        .catch { error ->
            unwrapFirestoreFlowError("observeUserProfile", error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val chapterProgressMap: StateFlow<Map<String, ChapterProgress>> = combine(
        firestoreRepository.observeChapterProgress(currentUserId)
            .catch { error ->
                unwrapFirestoreFlowError("observeChapterProgress", error)
                emit(emptyList())
            },
        localProgressDao.observeAll()
    ) { cloudList, localList ->
        val map = mutableMapOf<String, ChapterProgress>()
        localList.forEach { local ->
            map[local.chapterId] = ChapterProgress(
                id = local.chapterId,
                userId = currentUserId,
                chapterId = local.chapterId,
                subject = local.division,
                completedTopicIds = local.completedTopicsCsv.split(",").filter { it.isNotBlank() },
                masteryPercent = local.masteryPercent.toLong(),
                notesBookmarked = local.notesBookmarked,
                lastOpenedTopicId = local.lastOpenedTopicId
            )
        }
        cloudList.forEach { cloud ->
            map[cloud.chapterId] = cloud
        }
        map
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyMap()
    )

    val examAttempts: StateFlow<List<ExamAttempt>> = combine(
        firestoreRepository.observeExamAttempts(currentUserId)
            .catch { error ->
                unwrapFirestoreFlowError("observeExamAttempts", error)
                emit(emptyList())
            },
        localExamDao.observeAll()
    ) { cloud, local ->
        if (cloud.isNotEmpty()) cloud
        else local.map {
            ExamAttempt(
                id = "local_${it.id}",
                userId = currentUserId,
                chapterId = it.chapterId,
                examType = it.examType,
                subject = it.subject,
                score = it.score.toLong(),
                maxScore = it.maxScore.toLong(),
                weakTopics = it.weakTopicsCsv.split(",").filter { s -> s.isNotBlank() },
                feedbackSummary = it.feedbackSummary
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val savedNotes: StateFlow<List<SavedStudyNote>> = combine(
        firestoreRepository.observeSavedNotes(currentUserId)
            .catch { error ->
                unwrapFirestoreFlowError("observeSavedNotes", error)
                emit(emptyList())
            },
        offlineNoteDao.observeAll()
    ) { cloud, local ->
        if (cloud.isNotEmpty()) cloud
        else local.map {
            SavedStudyNote(
                id = "local_${it.id}",
                userId = currentUserId,
                chapterId = it.chapterId,
                title = it.title,
                content = it.content,
                sourceType = it.sourceType,
                medium = it.medium
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            textbookRepository.ensureCatalogueSeeded()
            firestoreRepository.ensureAndUpdateUserProfile(
                userId = currentUserId,
                displayName = displayName,
                preferredMedium = _uiState.value.selectedLanguage.name,
                selectedSubject = _uiState.value.selectedSubject.name,
                selectedChapterId = _uiState.value.selectedChapterId,
                dailyTargetMinutes = _uiState.value.dailyTargetMinutes.toLong()
            )
        }
        viewModelScope.launch { textbookRepository.reconcileActiveDownloads() }
        verifyOfficialSyllabusAndPyqs()
    }

    fun selectSubject(filter: SubjectFilter) {
        val chaptersForSub = SyllabusRepository.getChaptersForSubject(filter)
        val currentCh = _uiState.value.selectedChapterId
        val nextCh = if (chaptersForSub.any { it.id == currentCh }) {
            SyllabusRepository.getChapterById(currentCh)
        } else {
            chaptersForSub.firstOrNull() ?: SyllabusRepository.chapters.first()
        }
        _uiState.value = _uiState.value.copy(
            selectedSubject = filter,
            selectedChapterId = nextCh.id,
            selectedTopicId = nextCh.topics.firstOrNull()?.id.orEmpty(),
            recentChapterIds = (listOf(nextCh.id) + _uiState.value.recentChapterIds).distinct().take(5)
        )
        syncPreferencesToCloud()
    }

    fun selectChapter(chapterId: String) {
        val ch = SyllabusRepository.getChapterById(chapterId)
        _uiState.value = _uiState.value.copy(
            selectedChapterId = ch.id,
            selectedTopicId = ch.topics.firstOrNull()?.id.orEmpty(),
            recentChapterIds = (listOf(ch.id) + _uiState.value.recentChapterIds).distinct().take(5)
        )
        syncPreferencesToCloud()
    }

    fun selectTopic(topicId: String) {
        _uiState.value = _uiState.value.copy(selectedTopicId = topicId)
    }

    fun selectLanguage(language: StudyLanguage) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
        syncPreferencesToCloud()
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun resetSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
    }

    fun updateDailyTarget(minutes: Int) {
        val safe = minutes.coerceIn(10, 300)
        _uiState.value = _uiState.value.copy(dailyTargetMinutes = safe)
        syncPreferencesToCloud()
    }

    fun toggleTopicCompleted(chapter: SyllabusChapter, topicId: String) {
        viewModelScope.launch {
            val current = chapterProgressMap.value[chapter.id]
            val existingSet = current?.completedTopicIds?.toMutableSet() ?: mutableSetOf()
            if (existingSet.contains(topicId)) existingSet.remove(topicId) else existingSet.add(topicId)
            val topicCount = chapter.topics.size.coerceAtLeast(1)
            val mastery = ((existingSet.size * 100) / topicCount).coerceIn(0, 100)
            val bookmarked = current?.notesBookmarked ?: false

            localProgressDao.upsert(
                LocalChapterProgressEntity(
                    chapterId = chapter.id,
                    division = chapter.division.name,
                    completedTopicsCsv = existingSet.joinToString(","),
                    masteryPercent = mastery,
                    notesBookmarked = bookmarked,
                    lastOpenedTopicId = topicId,
                    lastRevisedAtMillis = System.currentTimeMillis()
                )
            )

            firestoreRepository.upsertChapterProgress(
                userId = currentUserId,
                chapterId = chapter.id,
                subject = chapter.division.name,
                completedTopicIds = existingSet.toList(),
                masteryPercent = mastery,
                notesBookmarked = bookmarked,
                lastOpenedTopicId = topicId
            )
        }
    }

    fun saveChapterNotesOffline(chapter: SyllabusChapter) {
        viewModelScope.launch {
            val content = buildString {
                appendLine("${chapter.titleEn} (${chapter.titleKn})")
                appendLine("Source: ${chapter.textbookSourceRef}")
                appendLine()
                chapter.revisionNotes.forEach { appendLine("• $it") }
                if (chapter.formulas.isNotEmpty()) {
                    appendLine("\nFormulas:")
                    chapter.formulas.forEach { appendLine("- ${it.name}: ${it.formula} [${it.siUnit}]") }
                }
            }
            val mediumStr = if (_uiState.value.selectedLanguage == StudyLanguage.KANNADA) "KANNADA" else "ENGLISH"
            offlineNoteDao.insert(
                OfflineNoteEntity(
                    chapterId = chapter.id,
                    title = "Revision Pack: ${chapter.titleEn}",
                    content = content,
                    sourceType = "OFFICIAL_TEXTBOOK",
                    medium = mediumStr,
                    savedAtMillis = System.currentTimeMillis()
                )
            )
            val res = firestoreRepository.saveStudyNote(
                userId = currentUserId,
                chapterId = chapter.id,
                title = "Revision Pack: ${chapter.titleEn}",
                content = content,
                sourceType = "OFFICIAL_TEXTBOOK",
                medium = mediumStr
            )
            _uiState.value = _uiState.value.copy(
                statusBannerMessage = if (res.isSuccess) {
                    "Saved ${chapter.titleEn} notes offline & synced to cloud."
                } else {
                    "Saved ${chapter.titleEn} notes locally for offline access."
                }
            )
        }
    }

    fun downloadTextbook(bookId: String) {
        viewModelScope.launch {
            textbookRepository.startDownload(bookId)
        }
    }

    fun downloadSelectedOfficialPdf(bookId: String, selectedUrl: String) {
        viewModelScope.launch {
            val result = textbookRepository.downloadSelectedOfficialPdf(bookId, selectedUrl)
            _uiState.value = _uiState.value.copy(
                statusBannerMessage = if (result.isSuccess) {
                    "The download is complete or already in progress. Check the textbook card; only a PDF that passes file verification can be opened offline."
                } else {
                    result.exceptionOrNull()?.localizedMessage ?: "The textbook PDF download could not be completed."
                }
            )
        }
    }

    fun updateBookReadingState(bookId: String, page: Int, bookmarks: List<Int>) {
        viewModelScope.launch {
            textbookRepository.updateReadingProgress(bookId, page, bookmarks)
        }
    }

    fun verifyOfficialSyllabusAndPyqs() {
        viewModelScope.launch {
            val syllabusStatus = SyllabusRepository.verifyOfficialPortalsLive()
            val pyqStatus = PyqRepository.checkPaperArchiveIndexLive()
            _uiState.value = _uiState.value.copy(
                verificationInfo = syllabusStatus,
                pyqDiscoveryStatus = pyqStatus
            )
        }
    }

    fun dismissBanner() {
        _uiState.value = _uiState.value.copy(statusBannerMessage = null, syncErrorMessage = null)
    }

    private fun syncPreferencesToCloud() {
        viewModelScope.launch {
            val state = _uiState.value
            firestoreRepository.ensureAndUpdateUserProfile(
                userId = currentUserId,
                displayName = displayName,
                preferredMedium = state.selectedLanguage.name,
                selectedSubject = state.selectedSubject.name,
                selectedChapterId = state.selectedChapterId,
                dailyTargetMinutes = state.dailyTargetMinutes.toLong()
            )
        }
    }

    private fun unwrapFirestoreFlowError(source: String, error: Throwable) {
        val fsError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (fsError == null && error is CancellationException) throw error
        val root = fsError ?: error
        Log.w(TAG, "Firestore stream warning ($source)", root)
        _uiState.value = _uiState.value.copy(
            syncErrorMessage = root.localizedMessage ?: "Cloud sync temporarily unavailable; offline mode active."
        )
    }

    private companion object {
        const val TAG = "SharedCurriculumVM"
    }
}
