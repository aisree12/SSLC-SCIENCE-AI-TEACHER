package com.example.data.ai

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.curriculum.KnowledgeRetrievalResult
import com.example.data.curriculum.ScienceKnowledgeEngine
import com.example.model.ScienceDivision
import com.example.model.StudyLanguage
import com.example.model.TeachingMode
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

sealed interface AiProviderStatus {
    data object GeminiCloudReady : AiProviderStatus
    data object OfflineKnowledgeOnly : AiProviderStatus
}

data class AiStreamEvent(
    val accumulatedText: String,
    val isComplete: Boolean,
    val sourceCitation: String,
    val detectedQuestionType: String,
    val providerLabel: String
)

data class AnswerEvaluationResult(
    val marksAwarded: Int,
    val maxMarks: Int,
    val missingPoints: List<String>,
    val scientificErrors: List<String>,
    val suggestedImprovement: String,
    val modelAnswer: String,
    val isAiEvaluated: Boolean
)

interface ScienceAiProvider {
    val providerName: String
    fun streamScienceAnswer(
        question: String,
        chapterId: String,
        language: StudyLanguage,
        mode: TeachingMode
    ): Flow<AiStreamEvent>

    suspend fun evaluateDescriptiveAnswer(
        question: String,
        studentAnswer: String,
        division: ScienceDivision,
        maxMarks: Int,
        expectedHint: String
    ): AnswerEvaluationResult
}

class AiTeacherRepository(private val context: Context) : ScienceAiProvider {

    override val providerName: String = "Firebase AI Logic (Gemini 3.8 Flash) + Local Science RAG"

    private val chatModel by lazy {
        Firebase.ai.generativeModel(modelName = "gemini-3.8-flash")
    }

    private val evaluationSchema by lazy {
        Schema.obj(
            properties = mapOf(
                "marksAwarded" to Schema.integer(description = "Marks earned from 0 up to maxMarks"),
                "missingPoints" to Schema.array(
                    items = Schema.string(),
                    description = "Key expected KSEAB points missing in student answer"
                ),
                "scientificErrors" to Schema.array(
                    items = Schema.string(),
                    description = "Any scientific or unit/equation errors"
                ),
                "suggestedImprovement" to Schema.string(description = "Supportive, actionable improvement advice"),
                "modelAnswer" to Schema.string(description = "Concise board-exam model answer")
            )
        )
    }

    private val evaluatorModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = evaluationSchema
            }
        )
    }

    fun isNetworkAvailable(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }

    fun currentProviderStatus(): AiProviderStatus {
        return if (isNetworkAvailable()) AiProviderStatus.GeminiCloudReady else AiProviderStatus.OfflineKnowledgeOnly
    }

    override fun streamScienceAnswer(
        question: String,
        chapterId: String,
        language: StudyLanguage,
        mode: TeachingMode
    ): Flow<AiStreamEvent> = flow {
        val retrieval: KnowledgeRetrievalResult = ScienceKnowledgeEngine.retrieve(question, chapterId)

        if (!retrieval.isInSyllabus) {
            val redirectMsg = ScienceKnowledgeEngine.generateOfflineFallbackAnswer(
                query = question,
                language = language,
                mode = mode,
                retrieval = retrieval
            )
            emit(
                AiStreamEvent(
                    accumulatedText = redirectMsg,
                    isComplete = true,
                    sourceCitation = "Karnataka SSLC Science Syllabus Guard",
                    detectedQuestionType = "Off-Topic Redirect",
                    providerLabel = "Syllabus Guard"
                )
            )
            return@flow
        }

        if (!isNetworkAvailable()) {
            val offlineAnswer = ScienceKnowledgeEngine.generateOfflineFallbackAnswer(
                query = question,
                language = language,
                mode = mode,
                retrieval = retrieval
            )
            emit(
                AiStreamEvent(
                    accumulatedText = offlineAnswer,
                    isComplete = true,
                    sourceCitation = retrieval.sourceCitation,
                    detectedQuestionType = retrieval.detectedQuestionType,
                    providerLabel = "Offline Local Science Knowledge Engine"
                )
            )
            return@flow
        }

        val systemPrompt = ScienceKnowledgeEngine.buildGeminiSystemPrompt(
            language = language,
            mode = mode,
            questionType = retrieval.detectedQuestionType,
            ragContext = retrieval.contextBlock
        )
        val fullPrompt = "$systemPrompt\n\nStudent Question:\n$question"

        val accumulated = StringBuilder()
        try {
            withTimeout(60_000L) {
                chatModel.generateContentStream(fullPrompt).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(
                            AiStreamEvent(
                                accumulatedText = accumulated.toString(),
                                isComplete = false,
                                sourceCitation = retrieval.sourceCitation,
                                detectedQuestionType = retrieval.detectedQuestionType,
                                providerLabel = "Gemini AI Teacher (Grounded in KTBS)"
                            )
                        )
                    }
                }
            }
            if (accumulated.isBlank()) {
                throw IllegalStateException("Gemini AI returned an empty response.")
            }
            emit(
                AiStreamEvent(
                    accumulatedText = accumulated.toString(),
                    isComplete = true,
                    sourceCitation = retrieval.sourceCitation,
                    detectedQuestionType = retrieval.detectedQuestionType,
                    providerLabel = "Gemini AI Teacher (Grounded in KTBS)"
                )
            )
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini AI request timed out", e)
            throw IllegalStateException("AI Teacher timed out after 60 seconds. Check connection or retry.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini AI generation failed", e)
            throw e
        }
    }

    override suspend fun evaluateDescriptiveAnswer(
        question: String,
        studentAnswer: String,
        division: ScienceDivision,
        maxMarks: Int,
        expectedHint: String
    ): AnswerEvaluationResult {
        if (studentAnswer.isBlank()) {
            return AnswerEvaluationResult(
                marksAwarded = 0,
                maxMarks = maxMarks,
                missingPoints = listOf("No answer submitted."),
                scientificErrors = emptyList(),
                suggestedImprovement = "Write key scientific terms, formulas, or equations before submitting.",
                modelAnswer = expectedHint,
                isAiEvaluated = false
            )
        }

        if (!isNetworkAvailable()) {
            return deterministicRubricFallback(studentAnswer, division, maxMarks, expectedHint)
        }

        val rubric = when (division) {
            ScienceDivision.PHYSICS -> "Physics rubric: Formula, Substitution, Calculation, SI Unit, Conclusion."
            ScienceDivision.CHEMISTRY -> "Chemistry rubric: Correct reactants, Correct products, Balanced equation, Reaction type, Scientific explanation."
            ScienceDivision.BIOLOGY -> "Biology rubric: Correct biological terminology, Required points, Sequence of process, Diagram/labels where relevant."
        }

        val prompt = """
Evaluate this Karnataka SSLC Class 10 ${division.displayName} student answer out of $maxMarks marks.
$rubric
Note: This is an AI practice evaluation, not an official KSEAB board score. Be encouraging and accurate.

Question ($maxMarks marks): $question
Reference Key / Expected Points: $expectedHint
Student Answer: $studentAnswer
        """.trimIndent()

        return try {
            val response = withTimeout(45_000L) {
                evaluatorModel.generateContent(prompt)
            }
            val jsonText = response.text ?: throw IllegalStateException("Empty evaluation response")
            val json = JSONObject(jsonText)
            val missingArr = json.optJSONArray("missingPoints")
            val errorsArr = json.optJSONArray("scientificErrors")
            AnswerEvaluationResult(
                marksAwarded = json.optInt("marksAwarded", 0).coerceIn(0, maxMarks),
                maxMarks = maxMarks,
                missingPoints = List(missingArr?.length() ?: 0) { idx -> missingArr!!.getString(idx) },
                scientificErrors = List(errorsArr?.length() ?: 0) { idx -> errorsArr!!.getString(idx) },
                suggestedImprovement = json.optString("suggestedImprovement", "Review the textbook definition and key terms."),
                modelAnswer = json.optString("modelAnswer", expectedHint),
                isAiEvaluated = true
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI evaluation failed; falling back to local rubric", e)
            deterministicRubricFallback(studentAnswer, division, maxMarks, expectedHint)
        }
    }

    private fun deterministicRubricFallback(
        studentAnswer: String,
        division: ScienceDivision,
        maxMarks: Int,
        expectedHint: String
    ): AnswerEvaluationResult {
        val hintTokens = expectedHint.lowercase()
            .split(Regex("[^a-z0-9]+"))
            .filter { it.length >= 4 }
            .distinct()
        val answerLower = studentAnswer.lowercase()
        val matched = hintTokens.count { answerLower.contains(it) }
        val ratio = if (hintTokens.isEmpty()) 0.5f else (matched.toFloat() / hintTokens.size.coerceAtMost(8))
        val score = (ratio * maxMarks).toInt().coerceIn(if (studentAnswer.length > 15) 1 else 0, maxMarks)

        return AnswerEvaluationResult(
            marksAwarded = score,
            maxMarks = maxMarks,
            missingPoints = if (score < maxMarks) listOf("Check missing keywords from reference answer (${division.displayName} rubric).") else emptyList(),
            scientificErrors = emptyList(),
            suggestedImprovement = "Offline rubric estimate: compare your answer carefully with the model answer below.",
            modelAnswer = expectedHint,
            isAiEvaluated = false
        )
    }

    private companion object {
        const val TAG = "AiTeacherRepo"
    }
}
