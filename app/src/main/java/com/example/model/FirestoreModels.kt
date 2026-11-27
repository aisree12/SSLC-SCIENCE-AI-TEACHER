package com.example.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Student",
    val preferredMedium: String = "ENGLISH",
    val selectedSubject: String = "ALL",
    val selectedChapterId: String = "chem_reactions",
    val dailyTargetMinutes: Long = 45L,
    val streakDays: Long = 1L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "displayName" to displayName.take(80).ifBlank { "Student" },
        "preferredMedium" to preferredMedium,
        "selectedSubject" to selectedSubject,
        "selectedChapterId" to selectedChapterId.ifBlank { "chem_reactions" },
        "dailyTargetMinutes" to dailyTargetMinutes.coerceIn(10L, 300L),
        "streakDays" to streakDays.coerceIn(0L, 3650L),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@Keep
@IgnoreExtraProperties
data class ChapterProgress(
    @DocumentId val id: String = "",
    val userId: String = "",
    val chapterId: String = "",
    val subject: String = "CHEMISTRY",
    val completedTopicIds: List<String> = emptyList(),
    val masteryPercent: Long = 0L,
    val notesBookmarked: Boolean = false,
    val lastOpenedTopicId: String = "",
    val lastRevisedAt: Timestamp? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class ExamAttempt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val chapterId: String = "",
    val examType: String = "CHAPTER_TEST",
    val subject: String = "ALL",
    val score: Long = 0L,
    val maxScore: Long = 20L,
    val weakTopics: List<String> = emptyList(),
    val feedbackSummary: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class SavedStudyNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val chapterId: String = "",
    val title: String = "",
    val content: String = "",
    val sourceType: String = "STUDENT_NOTE",
    val medium: String = "ENGLISH",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class AiConversationRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val chapterId: String = "",
    val question: String = "",
    val answer: String = "",
    val teachingMode: String = "Simple Explanation",
    val language: String = "ENGLISH",
    val sourceCitation: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
