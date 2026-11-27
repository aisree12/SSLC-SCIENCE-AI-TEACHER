package com.example.data.remote

import android.content.Context
import com.example.R
import com.example.firebase.OperationType
import com.example.firebase.handleFirestoreError
import com.example.model.AiConversationRecord
import com.example.model.ChapterProgress
import com.example.model.ExamAttempt
import com.example.model.SavedStudyNote
import com.example.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(): String {
        return auth.currentUser?.uid?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        if (userId.isBlank() || auth.currentUser?.uid != userId) {
            emit(null)
            return@flow
        }
        val docRef = db.collection("users").document(userId)
        emitAll(
            docRef.snapshots()
                .map { snapshot ->
                    snapshot.toObject(
                        UserProfile::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    unwrapAndLog(error, OperationType.GET, docRef.path, userId)
                    throw error
                }
        )
    }

    suspend fun ensureAndUpdateUserProfile(
        userId: String,
        displayName: String,
        preferredMedium: String,
        selectedSubject: String,
        selectedChapterId: String,
        dailyTargetMinutes: Long
    ): Result<Unit> {
        val docPath = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch." }
            val docRef = db.collection("users").document(uid)
            val existing = docRef.get().await()
            val safeDisplayName = displayName.trim().take(80).ifBlank { "SSLC Student" }
            val safeChapterId = selectedChapterId.trim().take(64).ifBlank { "chem_reactions" }
            val safeMinutes = dailyTargetMinutes.coerceIn(10L, 300L)

            if (!existing.exists()) {
                val payload = mapOf(
                    "userId" to uid,
                    "displayName" to safeDisplayName,
                    "preferredMedium" to preferredMedium,
                    "selectedSubject" to selectedSubject,
                    "selectedChapterId" to safeChapterId,
                    "dailyTargetMinutes" to safeMinutes,
                    "streakDays" to 1L,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            } else {
                docRef.update(
                    mapOf(
                        "displayName" to safeDisplayName,
                        "preferredMedium" to preferredMedium,
                        "selectedSubject" to selectedSubject,
                        "selectedChapterId" to safeChapterId,
                        "dailyTargetMinutes" to safeMinutes,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docPath)
            Result.failure(e)
        }
    }

    fun observeChapterProgress(userId: String): Flow<List<ChapterProgress>> = flow {
        if (userId.isBlank() || auth.currentUser?.uid != userId) {
            emit(emptyList())
            return@flow
        }
        val path = "users/$userId/chapter_progress"
        emitAll(
            db.collection("users").document(userId).collection("chapter_progress")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ChapterProgress::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    unwrapAndLog(error, OperationType.LIST, path, userId)
                    throw error
                }
        )
    }

    suspend fun upsertChapterProgress(
        userId: String,
        chapterId: String,
        subject: String,
        completedTopicIds: List<String>,
        masteryPercent: Int,
        notesBookmarked: Boolean,
        lastOpenedTopicId: String
    ): Result<Unit> {
        val safeChapterId = chapterId.trim().take(64)
        val docPath = "users/$userId/chapter_progress/$safeChapterId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch." }
            require(safeChapterId.isNotBlank()) { "Chapter ID must not be blank" }
            val docRef = db.collection("users").document(uid)
                .collection("chapter_progress").document(safeChapterId)
            val existing = docRef.get().await()
            val boundedTopics = completedTopicIds.take(40)
            val boundedMastery = masteryPercent.coerceIn(0, 100).toLong()
            val safeTopicId = lastOpenedTopicId.trim().take(64)

            if (!existing.exists()) {
                val payload = mapOf(
                    "userId" to uid,
                    "chapterId" to safeChapterId,
                    "subject" to subject,
                    "completedTopicIds" to boundedTopics,
                    "masteryPercent" to boundedMastery,
                    "notesBookmarked" to notesBookmarked,
                    "lastOpenedTopicId" to safeTopicId,
                    "lastRevisedAt" to FieldValue.serverTimestamp(),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            } else {
                docRef.update(
                    mapOf(
                        "completedTopicIds" to boundedTopics,
                        "masteryPercent" to boundedMastery,
                        "notesBookmarked" to notesBookmarked,
                        "lastOpenedTopicId" to safeTopicId,
                        "lastRevisedAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docPath)
            Result.failure(e)
        }
    }

    fun observeExamAttempts(userId: String): Flow<List<ExamAttempt>> = flow {
        if (userId.isBlank() || auth.currentUser?.uid != userId) {
            emit(emptyList())
            return@flow
        }
        val path = "users/$userId/exam_attempts"
        emitAll(
            db.collection("users").document(userId).collection("exam_attempts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ExamAttempt::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    unwrapAndLog(error, OperationType.LIST, path, userId)
                    throw error
                }
        )
    }

    suspend fun recordExamAttempt(
        userId: String,
        chapterId: String,
        examType: String,
        subject: String,
        score: Int,
        maxScore: Int,
        weakTopics: List<String>,
        feedbackSummary: String
    ): Result<Unit> {
        val path = "users/$userId/exam_attempts"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch." }
            val safeMax = maxScore.coerceIn(1, 100)
            val safeScore = score.coerceIn(0, safeMax)
            val docRef = db.collection("users").document(uid).collection("exam_attempts").document()
            val payload = mapOf(
                "userId" to uid,
                "chapterId" to chapterId.take(64).ifBlank { "MOCK_FULL" },
                "examType" to examType,
                "subject" to subject,
                "score" to safeScore.toLong(),
                "maxScore" to safeMax.toLong(),
                "weakTopics" to weakTopics.take(20),
                "feedbackSummary" to feedbackSummary.take(1000),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    fun observeSavedNotes(userId: String): Flow<List<SavedStudyNote>> = flow {
        if (userId.isBlank() || auth.currentUser?.uid != userId) {
            emit(emptyList())
            return@flow
        }
        val path = "users/$userId/saved_notes"
        emitAll(
            db.collection("users").document(userId).collection("saved_notes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(100)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        SavedStudyNote::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    unwrapAndLog(error, OperationType.LIST, path, userId)
                    throw error
                }
        )
    }

    suspend fun saveStudyNote(
        userId: String,
        chapterId: String,
        title: String,
        content: String,
        sourceType: String,
        medium: String
    ): Result<Unit> {
        val path = "users/$userId/saved_notes"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch." }
            val docRef = db.collection("users").document(uid).collection("saved_notes").document()
            val payload = mapOf(
                "userId" to uid,
                "chapterId" to chapterId.take(64).ifBlank { "chem_reactions" },
                "title" to title.trim().take(150).ifBlank { "Science Note" },
                "content" to content.trim().take(5000).ifBlank { "Saved note" },
                "sourceType" to sourceType,
                "medium" to medium,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    fun observeAiConversations(userId: String): Flow<List<AiConversationRecord>> = flow {
        if (userId.isBlank() || auth.currentUser?.uid != userId) {
            emit(emptyList())
            return@flow
        }
        val path = "users/$userId/ai_conversations"
        emitAll(
            db.collection("users").document(userId).collection("ai_conversations")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        AiConversationRecord::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    unwrapAndLog(error, OperationType.LIST, path, userId)
                    throw error
                }
        )
    }

    suspend fun saveAiConversation(
        userId: String,
        chapterId: String,
        question: String,
        answer: String,
        teachingMode: String,
        language: String,
        sourceCitation: String
    ): Result<Unit> {
        val path = "users/$userId/ai_conversations"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user mismatch." }
            val docRef = db.collection("users").document(uid).collection("ai_conversations").document()
            val payload = mapOf(
                "userId" to uid,
                "chapterId" to chapterId.take(64),
                "question" to question.trim().take(1500).ifBlank { "Science question" },
                "answer" to answer.trim().take(12000).ifBlank { "Answer" },
                "teachingMode" to teachingMode.take(50).ifBlank { "Simple Explanation" },
                "language" to language,
                "sourceCitation" to sourceCitation.take(300),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    private fun unwrapAndLog(
        error: Throwable,
        operationType: OperationType,
        path: String,
        expectedUid: String
    ) {
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == expectedUid) {
            handleFirestoreError(exceptionToLog, operationType, path)
        }
    }
}
