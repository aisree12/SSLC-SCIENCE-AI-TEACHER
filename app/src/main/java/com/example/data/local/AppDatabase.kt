package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TextbookDownloadEntity::class,
        LocalChapterProgressEntity::class,
        OfflineNoteEntity::class,
        LocalExamHistoryEntity::class,
        CachedAiQaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun textbookDownloadDao(): TextbookDownloadDao
    abstract fun localProgressDao(): LocalProgressDao
    abstract fun offlineNoteDao(): OfflineNoteDao
    abstract fun localExamHistoryDao(): LocalExamHistoryDao
    abstract fun cachedAiQaDao(): CachedAiQaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sslc_science_teacher.db"
                ).fallbackToDestructiveMigration(dropAllTables = false)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
