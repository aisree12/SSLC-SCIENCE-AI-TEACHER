package com.example.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiProviderStatus
import com.example.data.ai.AiTeacherRepository
import com.example.data.local.AppDatabase
import com.example.data.local.CachedAiQaEntity
import com.example.data.remote.FirestoreRepository
import com.example.model.AiConversationRecord
import com.example.model.StudyLanguage
import com.example.model.TeachingMode
import com.google.firebase.Timestamp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

data class AiChatMessage(
    val id: String,
    val isStudent: Boolean,
    val text: String,
    val teachingMode: TeachingMode,
    val language: StudyLanguage,
    val sourceCitation: String = "",
    val questionType: String = "",
    val providerLabel: String = ""
)

sealed interface AiTeacherUiState {
    data object Idle : AiTeacherUiState
    data object Loading : AiTeacherUiState
    data class Streaming(val partialText: String) : AiTeacherUiState
    data class Error(val message: String) : AiTeacherUiState
}

class AiTeacherViewModel(
    appContext: Context,
    private val firestoreRepository: FirestoreRepository,
    private val currentUserId: String
) : ViewModel() {

    private val aiRepository = AiTeacherRepository(appContext)
    private val cachedAiDao = AppDatabase.getInstance(appContext).cachedAiQaDao()

    private val _selectedMode = MutableStateFlow(TeachingMode.SIMPLE)
    val selectedMode: StateFlow<TeachingMode> = _selectedMode.asStateFlow()

    private val _messages = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val messages: StateFlow<List<AiChatMessage>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<AiTeacherUiState>(AiTeacherUiState.Idle)
    val uiState: StateFlow<AiTeacherUiState> = _uiState.asStateFlow()

    private val _draftQuestion = MutableStateFlow("")
    val draftQuestion: StateFlow<String> = _draftQuestion.asStateFlow()

    private val _providerStatus = MutableStateFlow<AiProviderStatus>(aiRepository.currentProviderStatus())
    val providerStatus: StateFlow<AiProviderStatus> = _providerStatus.asStateFlow()

    val savedHistory: StateFlow<List<AiConversationRecord>> = combine(
        firestoreRepository.observeAiConversations(currentUserId)
            .catch { error ->
                Log.w(TAG, "Cloud AI history unavailable; showing local history", error)
                emit(emptyList())
            },
        cachedAiDao.observeRecent()
    ) { cloud, local ->
        val localRecords = local.map { item ->
            AiConversationRecord(
                id = "local_${item.id}",
                userId = currentUserId,
                chapterId = item.chapterId,
                question = item.question,
                answer = item.answer,
                teachingMode = item.teachingMode,
                language = item.language,
                sourceCitation = item.sourceCitation,
                createdAt = Timestamp(Date(item.createdAtMillis))
            )
        }
        (cloud + localRecords)
            .distinctBy { it.id }
            .sortedByDescending { it.createdAt?.toDate()?.time ?: 0L }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun refreshProviderStatus() {
        _providerStatus.value = aiRepository.currentProviderStatus()
    }

    fun selectTeachingMode(mode: TeachingMode) {
        _selectedMode.value = mode
    }

    fun updateDraftQuestion(text: String) {
        _draftQuestion.value = text
    }

    fun askQuestion(
        question: String,
        chapterId: String,
        language: StudyLanguage
    ) {
        val trimmed = question.trim()
        if (trimmed.isEmpty()) return

        refreshProviderStatus()
        val mode = _selectedMode.value
        val userMsg = AiChatMessage(
            id = "u_${System.currentTimeMillis()}",
            isStudent = true,
            text = trimmed,
            teachingMode = mode,
            language = language
        )
        _messages.value = _messages.value + userMsg
        _draftQuestion.value = ""
        _uiState.value = AiTeacherUiState.Loading

        viewModelScope.launch {
            try {
                var finalAnswer = ""
                var finalCitation = ""
                var finalType = ""
                var finalProvider = ""

                aiRepository.streamScienceAnswer(
                    question = trimmed,
                    chapterId = chapterId,
                    language = language,
                    mode = mode
                ).collect { event ->
                    finalAnswer = event.accumulatedText
                    finalCitation = event.sourceCitation
                    finalType = event.detectedQuestionType
                    finalProvider = event.providerLabel
                    if (!event.isComplete) {
                        _uiState.value = AiTeacherUiState.Streaming(event.accumulatedText)
                    }
                }

                if (finalAnswer.isNotBlank()) {
                    val botMsg = AiChatMessage(
                        id = "a_${System.currentTimeMillis()}",
                        isStudent = false,
                        text = finalAnswer,
                        teachingMode = mode,
                        language = language,
                        sourceCitation = finalCitation,
                        questionType = finalType,
                        providerLabel = finalProvider
                    )
                    _messages.value = _messages.value + botMsg
                    _uiState.value = AiTeacherUiState.Idle

                    cachedAiDao.insert(
                        CachedAiQaEntity(
                            chapterId = chapterId,
                            question = trimmed,
                            answer = finalAnswer,
                            teachingMode = mode.title,
                            language = language.name,
                            sourceCitation = finalCitation,
                            createdAtMillis = System.currentTimeMillis()
                        )
                    )
                    try {
                        firestoreRepository.saveAiConversation(
                            userId = currentUserId,
                            chapterId = chapterId,
                            question = trimmed,
                            answer = finalAnswer,
                            teachingMode = mode.title,
                            language = language.name,
                            sourceCitation = finalCitation
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        Log.w(TAG, "Cloud conversation save failed; local answer is retained", e)
                    }
                } else {
                    _uiState.value = AiTeacherUiState.Error("No response received from AI Teacher.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "AI Teacher query failed", e)
                _uiState.value = AiTeacherUiState.Error(
                    e.localizedMessage ?: "AI Teacher encountered an error. Please retry."
                )
            }
        }
    }

    fun clearError() {
        if (_uiState.value is AiTeacherUiState.Error) {
            _uiState.value = AiTeacherUiState.Idle
        }
    }

    private companion object {
        const val TAG = "AiTeacherVM"
    }
}
