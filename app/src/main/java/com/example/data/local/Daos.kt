package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TextbookDownloadDao {
    @Query("SELECT * FROM textbook_downloads ORDER BY medium ASC, part ASC")
    fun observeAll(): Flow<List<TextbookDownloadEntity>>

    @Query("SELECT * FROM textbook_downloads WHERE id = :id")
    suspend fun getById(id: String): TextbookDownloadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TextbookDownloadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TextbookDownloadEntity>)
}

@Dao
interface LocalProgressDao {
    @Query("SELECT * FROM local_chapter_progress")
    fun observeAll(): Flow<List<LocalChapterProgressEntity>>

    @Query("SELECT * FROM local_chapter_progress WHERE chapterId = :chapterId")
    suspend fun getByChapterId(chapterId: String): LocalChapterProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LocalChapterProgressEntity)
}

@Dao
interface OfflineNoteDao {
    @Query("SELECT * FROM offline_study_notes ORDER BY savedAtMillis DESC")
    fun observeAll(): Flow<List<OfflineNoteEntity>>

    @Query("SELECT * FROM offline_study_notes WHERE chapterId = :chapterId ORDER BY savedAtMillis DESC")
    fun observeForChapter(chapterId: String): Flow<List<OfflineNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: OfflineNoteEntity)

    @Query("DELETE FROM offline_study_notes WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface LocalExamHistoryDao {
    @Query("SELECT * FROM local_exam_history ORDER BY attemptedAtMillis DESC")
    fun observeAll(): Flow<List<LocalExamHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attempt: LocalExamHistoryEntity)
}

@Dao
interface CachedAiQaDao {
    @Query("SELECT * FROM cached_ai_qa ORDER BY createdAtMillis DESC LIMIT 50")
    fun observeRecent(): Flow<List<CachedAiQaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CachedAiQaEntity)

    @Query("DELETE FROM cached_ai_qa")
    suspend fun clearAll()
}
