package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiTeacherRepository
import com.example.data.ai.AnswerEvaluationResult
import com.example.data.curriculum.SyllabusRepository
import com.example.data.local.AppDatabase
import com.example.data.local.LocalExamHistoryEntity
import com.example.data.remote.FirestoreRepository
import com.example.model.ChapterQuizQuestion
import com.example.model.ScienceDivision
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AcademyTestMode(val label: String, val examTypeKey: String) {
    CHAPTER_MCQ("Chapter MCQ Test", "CHAPTER_TEST"),
    PHYSICS_NUMERICALS("Physics Numericals", "NUMERICAL_TEST"),
    CHEM_EQUATIONS("Chemical Equations", "EQUATION_TEST"),
    BIOLOGY_RUBRIC("Biology Long Answer", "CHAPTER_TEST"),
    KSEAB_MOCK_80("KSEAB 80-Mark Pattern Mock", "MOCK_80")
}

data class DescriptivePrompt(
    val id: String,
    val division: ScienceDivision,
    val chapterId: String,
    val marks: Int,
    val question: String,
    val expectedKeyPoints: String
)

data class AcademyUiState(
    val activeMode: AcademyTestMode = AcademyTestMode.CHAPTER_MCQ,
    val selectedAnswers: Map<String, Int> = emptyMap(),
    val mcqSubmitted: Boolean = false,
    val mcqScore: Int = 0,
    val mcqMaxScore: Int = 0,
    val descriptiveInput: String = "",
    val activeDescriptiveIndex: Int = 0,
    val isEvaluating: Boolean = false,
    val evaluationResult: AnswerEvaluationResult? = null,
    val evaluationError: String? = null
)

class ExamAcademyViewModel(
    appContext: Context,
    private val firestoreRepository: FirestoreRepository,
    private val currentUserId: String
) : ViewModel() {

    private val aiRepository = AiTeacherRepository(appContext)
    private val localExamDao = AppDatabase.getInstance(appContext).localExamHistoryDao()

    private val _uiState = MutableStateFlow(AcademyUiState())
    val uiState: StateFlow<AcademyUiState> = _uiState.asStateFlow()

    val descriptivePrompts: List<DescriptivePrompt> = listOf(
        DescriptivePrompt(
            id = "desc_phy_ohm",
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity",
            marks = 3,
            question = "An electric heater draws 4 A from a 220 V supply. Calculate its resistance and the heat generated in 2 minutes.",
            expectedKeyPoints = "Given: I = 4 A, V = 220 V, t = 120 s. Formula: R = V/I = 220/4 = 55 Ohm. Heat H = I^2*R*t = 16 * 55 * 120 = 105,600 J (or 105.6 kJ)."
        ),
        DescriptivePrompt(
            id = "desc_chem_feso4",
            division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_reactions",
            marks = 3,
            question = "Write the balanced chemical equation for thermal decomposition of ferrous sulphate crystals, identify reaction type, and state two observations.",
            expectedKeyPoints = "2FeSO4(s) -> Fe2O3(s) + SO2(g) + SO3(g). Type: Thermal Decomposition. Observations: Green crystals change to reddish-brown ferric oxide; smell of burning sulphur."
        ),
        DescriptivePrompt(
            id = "desc_bio_heart",
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_life_processes",
            marks = 3,
            question = "Explain double circulation in the human heart and why oxygenated and deoxygenated blood must remain separated.",
            expectedKeyPoints = "Blood passes twice through heart per cycle: Pulmonary circulation (Right ventricle -> Lungs -> Left atrium) and Systemic circulation (Left ventricle -> Body -> Right atrium). Separation ensures high oxygen supply needed by warm-blooded mammals/birds to maintain body temperature."
        )
    )

    fun setTestMode(mode: AcademyTestMode) {
        _uiState.value = _uiState.value.copy(
            activeMode = mode,
            selectedAnswers = emptyMap(),
            mcqSubmitted = false,
            evaluationResult = null,
            evaluationError = null
        )
    }

    fun selectMcqOption(questionId: String, optionIndex: Int) {
        if (_uiState.value.mcqSubmitted) return
        _uiState.value = _uiState.value.copy(
            selectedAnswers = _uiState.value.selectedAnswers + (questionId to optionIndex)
        )
    }

    fun questionsForChapterOrMock(chapterId: String, mode: AcademyTestMode): List<ChapterQuizQuestion> {
        return when (mode) {
            AcademyTestMode.KSEAB_MOCK_80 -> SyllabusRepository.chapters.flatMap { it.quizQuestions }
            AcademyTestMode.PHYSICS_NUMERICALS -> SyllabusRepository.chapters
                .filter { it.division == ScienceDivision.PHYSICS }
                .flatMap { it.quizQuestions }
            AcademyTestMode.CHEM_EQUATIONS -> SyllabusRepository.chapters
                .filter { it.division == ScienceDivision.CHEMISTRY }
                .flatMap { it.quizQuestions }
            AcademyTestMode.BIOLOGY_RUBRIC -> SyllabusRepository.chapters
                .filter { it.division == ScienceDivision.BIOLOGY }
                .flatMap { it.quizQuestions }
            AcademyTestMode.CHAPTER_MCQ -> SyllabusRepository.getChapterById(chapterId).quizQuestions
        }
    }

    fun submitMcqTest(chapterId: String, questions: List<ChapterQuizQuestion>) {
        if (questions.isEmpty()) return
        val selected = _uiState.value.selectedAnswers
        var earned = 0
        val weakTopics = mutableListOf<String>()
        questions.forEach { q ->
            if (selected[q.id] == q.correctIndex) {
                earned += q.marks
            } else if (q.topicId.isNotBlank()) {
                weakTopics.add(q.topicId)
            }
        }
        val max = questions.sumOf { it.marks }.coerceAtLeast(1)
        val mode = _uiState.value.activeMode
        val ch = SyllabusRepository.getChapterById(chapterId)
        val summary = "Scored $earned/$max in ${mode.label}"

        _uiState.value = _uiState.value.copy(
            mcqSubmitted = true,
            mcqScore = earned,
            mcqMaxScore = max
        )

        viewModelScope.launch {
            localExamDao.insert(
                LocalExamHistoryEntity(
                    chapterId = if (mode == AcademyTestMode.KSEAB_MOCK_80) "MOCK_FULL" else chapterId,
                    examType = mode.examTypeKey,
                    subject = if (mode == AcademyTestMode.KSEAB_MOCK_80) "ALL" else ch.division.name,
                    score = earned,
                    maxScore = max,
                    weakTopicsCsv = weakTopics.distinct().joinToString(","),
                    feedbackSummary = summary,
                    attemptedAtMillis = System.currentTimeMillis()
                )
            )
            firestoreRepository.recordExamAttempt(
                userId = currentUserId,
                chapterId = if (mode == AcademyTestMode.KSEAB_MOCK_80) "MOCK_FULL" else chapterId,
                examType = mode.examTypeKey,
                subject = if (mode == AcademyTestMode.KSEAB_MOCK_80) "ALL" else ch.division.name,
                score = earned,
                maxScore = max,
                weakTopics = weakTopics.distinct().take(20),
                feedbackSummary = summary
            )
        }
    }

    fun updateDescriptiveInput(text: String) {
        _uiState.value = _uiState.value.copy(descriptiveInput = text)
    }

    fun selectDescriptivePrompt(index: Int) {
        _uiState.value = _uiState.value.copy(
            activeDescriptiveIndex = index.coerceIn(0, descriptivePrompts.lastIndex),
            descriptiveInput = "",
            evaluationResult = null,
            evaluationError = null
        )
    }

    fun evaluateDescriptive() {
        val prompt = descriptivePrompts[_uiState.value.activeDescriptiveIndex]
        val answer = _uiState.value.descriptiveInput
        _uiState.value = _uiState.value.copy(isEvaluating = true, evaluationError = null)

        viewModelScope.launch {
            try {
                val result = aiRepository.evaluateDescriptiveAnswer(
                    question = prompt.question,
                    studentAnswer = answer,
                    division = prompt.division,
                    maxMarks = prompt.marks,
                    expectedHint = prompt.expectedKeyPoints
                )
                _uiState.value = _uiState.value.copy(
                    isEvaluating = false,
                    evaluationResult = result
                )
                localExamDao.insert(
                    LocalExamHistoryEntity(
                        chapterId = prompt.chapterId,
                        examType = "CHAPTER_TEST",
                        subject = prompt.division.name,
                        score = result.marksAwarded,
                        maxScore = result.maxMarks,
                        weakTopicsCsv = if (result.marksAwarded < result.maxMarks) prompt.chapterId else "",
                        feedbackSummary = result.suggestedImprovement.take(250),
                        attemptedAtMillis = System.currentTimeMillis()
                    )
                )
                firestoreRepository.recordExamAttempt(
                    userId = currentUserId,
                    chapterId = prompt.chapterId,
                    examType = "CHAPTER_TEST",
                    subject = prompt.division.name,
                    score = result.marksAwarded,
                    maxScore = result.maxMarks,
                    weakTopics = if (result.marksAwarded < result.maxMarks) listOf(prompt.chapterId) else emptyList(),
                    feedbackSummary = result.suggestedImprovement.take(1000)
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isEvaluating = false,
                    evaluationError = e.localizedMessage ?: "Evaluation failed"
                )
            }
        }
    }
}
