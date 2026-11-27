package com.example.data.curriculum

import com.example.model.RationalizedDeletion
import com.example.model.ScienceDivision
import com.example.model.SubjectFilter
import com.example.model.SyllabusChapter
import com.example.model.SyllabusVerificationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object SyllabusRepository {

    val chapters: List<SyllabusChapter> = (
        ChemistryChapters.all + BiologyChapters.all + PhysicsChapters.all
    ).sortedBy { it.chapterNumber }

    val rationalizedDeletions: List<RationalizedDeletion> = listOf(
        RationalizedDeletion(
            oldChapterTitle = "Periodic Classification of Elements",
            oldChapterNumber = 5,
            status = "Historical rationalized exclusion — 2026–27 confirmation pending",
            notes = "Listed in the local historical rationalized reference. Do not treat as a confirmed 2026–27 board exclusion until the current KSEAB syllabus or blueprint is checked.",
            sourceUrl = "https://kseab.karnataka.gov.in/"
        ),
        RationalizedDeletion(
            oldChapterTitle = "Sources of Energy",
            oldChapterNumber = 14,
            status = "Historical rationalized exclusion — 2026–27 confirmation pending",
            notes = "Listed in the local historical rationalized reference. Current-year board-exam exclusion has not been confirmed from a 2026–27 KSEAB document.",
            sourceUrl = "https://kseab.karnataka.gov.in/"
        ),
        RationalizedDeletion(
            oldChapterTitle = "Sustainable Management of Natural Resources",
            oldChapterNumber = 16,
            status = "Historical rationalized exclusion — 2026–27 confirmation pending",
            notes = "Listed in the local historical rationalized reference. Verify against the current 2026–27 KSEAB syllabus before using for exam planning.",
            sourceUrl = "https://textbooks.karnataka.gov.in/"
        )
    )

    val baselineVerification = SyllabusVerificationInfo(
        academicYear = "2026–2027",
        boardName = "KSEAB (Karnataka School Examination and Assessment Board)",
        editionLabel = "Local 13-chapter Science catalogue — current KSEAB notification cross-check pending",
        lastVerifiedDate = "Local catalogue loaded — not yet verified against the 2026–27 KSEAB notification",
        officialSources = listOf(
            "https://kseab.karnataka.gov.in/",
            "https://textbooks.karnataka.gov.in/",
            "https://diksha.gov.in/"
        ),
        activeChapterCount = chapters.size,
        statusMessage = "A 13-chapter local study catalogue is available. Its exact alignment with the 2026–27 KSEAB syllabus and deletions still needs official-document verification.",
        liveCheckSucceeded = false
    )

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .build()
    }

    fun getChaptersForSubject(filter: SubjectFilter): List<SyllabusChapter> {
        val division = filter.division ?: return chapters
        return chapters.filter { it.division == division }
    }

    fun getChapterById(id: String): SyllabusChapter {
        return chapters.firstOrNull { it.id == id } ?: chapters.first()
    }

    fun searchChaptersAndTopics(query: String, filter: SubjectFilter): List<SyllabusChapter> {
        val base = getChaptersForSubject(filter)
        val q = query.trim().lowercase()
        if (q.isBlank()) return base
        return base.filter { ch ->
            ch.titleEn.lowercase().contains(q) ||
                ch.titleKn.contains(q) ||
                ch.introduction.lowercase().contains(q) ||
                ch.topics.any {
                    it.title.lowercase().contains(q) ||
                        it.simpleExplanation.lowercase().contains(q) ||
                        it.kannadaTitle.contains(q)
                } ||
                ch.definitions.any { it.term.lowercase().contains(q) } ||
                ch.formulas.any { it.name.lowercase().contains(q) || it.formula.lowercase().contains(q) }
        }
    }

    suspend fun verifyOfficialPortalsLive(): SyllabusVerificationInfo = withContext(Dispatchers.IO) {
        val reachable = mutableListOf<String>()
        for (url in listOf("https://kseab.karnataka.gov.in/", "https://textbooks.karnataka.gov.in/")) {
            try {
                val request = Request.Builder().url(url).head().build()
                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful || response.code in 200..399) {
                        reachable.add(url)
                    }
                }
            } catch (_: Exception) {
                // Honest offline/unreachable status handled below
            }
        }
        if (reachable.isNotEmpty()) {
            baselineVerification.copy(
                lastVerifiedDate = "Official portal connectivity checked (${reachable.size}/2 reachable) — syllabus content not verified",
                statusMessage = "At least one official portal responded. This confirms connectivity only, not the current syllabus, chapter list, or rationalized deletions. The local 13-chapter catalogue remains provisional.",
                liveCheckSucceeded = false
            )
        } else {
            baselineVerification.copy(
                lastVerifiedDate = "Official portal connectivity check failed — local catalogue only",
                statusMessage = "Official portal could not be reached. Displaying the local 13-chapter study catalogue; current KSEAB alignment has not been verified.",
                liveCheckSucceeded = false
            )
        }
    }

    fun divisionForChapter(chapterId: String): ScienceDivision {
        return getChapterById(chapterId).division
    }
}
