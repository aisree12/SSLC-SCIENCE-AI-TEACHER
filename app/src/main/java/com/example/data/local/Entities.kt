package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textbook_downloads")
data class TextbookDownloadEntity(
    @PrimaryKey val id: String,
    val medium: String,
    val part: Int,
    val title: String,
    val kannadaTitle: String,
    val academicYear: String,
    val edition: String,
    val officialUrl: String,
    val localFilePath: String?,
    val downloadManagerId: Long?,
    val status: String,
    val progressPercent: Int,
    val sha256Checksum: String?,
    val lastReadPage: Int,
    val bookmarkedPagesCsv: String,
    val statusNote: String,
    val updatedAtMillis: Long
)

@Entity(tableName = "local_chapter_progress")
data class LocalChapterProgressEntity(
    @PrimaryKey val chapterId: String,
    val division: String,
    val completedTopicsCsv: String,
    val masteryPercent: Int,
    val notesBookmarked: Boolean,
    val lastOpenedTopicId: String,
    val lastRevisedAtMillis: Long
)

@Entity(tableName = "offline_study_notes")
data class OfflineNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val chapterId: String,
    val title: String,
    val content: String,
    val sourceType: String,
    val medium: String,
    val savedAtMillis: Long
)

@Entity(tableName = "local_exam_history")
data class LocalExamHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val chapterId: String,
    val examType: String,
    val subject: String,
    val score: Int,
    val maxScore: Int,
    val weakTopicsCsv: String,
    val feedbackSummary: String,
    val attemptedAtMillis: Long
)

@Entity(tableName = "cached_ai_qa")
data class CachedAiQaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val chapterId: String,
    val question: String,
    val answer: String,
    val teachingMode: String,
    val language: String,
    val sourceCitation: String,
    val createdAtMillis: Long
)
