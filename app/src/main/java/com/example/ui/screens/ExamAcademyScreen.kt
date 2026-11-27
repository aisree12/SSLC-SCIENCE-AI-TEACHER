package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.curriculum.SyllabusRepository
import com.example.model.ChapterQuizQuestion
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.ui.components.DivisionBadge
import com.example.ui.components.UnifiedCurriculumControls
import com.example.viewmodel.AcademyTestMode
import com.example.viewmodel.AcademyUiState
import com.example.viewmodel.CurriculumUiState
import com.example.viewmodel.DescriptivePrompt

@Composable
fun ExamAcademyScreen(
    curriculumState: CurriculumUiState,
    academyState: AcademyUiState,
    descriptivePrompts: List<DescriptivePrompt>,
    questionsProvider: (chapterId: String, mode: AcademyTestMode) -> List<ChapterQuizQuestion>,
    onSelectSubject: (SubjectFilter) -> Unit,
    onSelectChapter: (String) -> Unit,
    onSelectTopic: (String) -> Unit,
    onSelectLanguage: (StudyLanguage) -> Unit,
    onSelectMode: (AcademyTestMode) -> Unit,
    onSelectMcqOption: (questionId: String, optionIndex: Int) -> Unit,
    onSubmitMcqTest: (chapterId: String, questions: List<ChapterQuizQuestion>) -> Unit,
    onSelectDescriptivePrompt: (Int) -> Unit,
    onUpdateDescriptiveInput: (String) -> Unit,
    onEvaluateDescriptive: () -> Unit
) {
    val activeQuestions = remember(curriculumState.selectedChapterId, academyState.activeMode) {
        questionsProvider(curriculumState.selectedChapterId, academyState.activeMode)
    }
    val activeDescriptive = descriptivePrompts[academyState.activeDescriptiveIndex.coerceIn(0, descriptivePrompts.lastIndex)]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("exam_academy_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Science Exam Academy",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "KSEAB 80-mark pattern mocks, Physics numericals, Chemistry equations & AI Rubric feedback",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    UnifiedCurriculumControls(
                        selectedSubject = curriculumState.selectedSubject,
                        chapters = curriculumState.availableChapters.ifEmpty { SyllabusRepository.chapters },
                        selectedChapter = curriculumState.selectedChapter,
                        selectedTopicId = curriculumState.selectedTopicId,
                        selectedLanguage = curriculumState.selectedLanguage,
                        onSubjectSelected = onSelectSubject,
                        onChapterSelected = onSelectChapter,
                        onTopicSelected = onSelectTopic,
                        onLanguageSelected = onSelectLanguage,
                        showTopicSelector = false
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AcademyTestMode.entries.forEach { mode ->
                            FilterChip(
                                selected = academyState.activeMode == mode,
                                onClick = { onSelectMode(mode) },
                                label = { Text(mode.label) },
                                modifier = Modifier.testTag("academy_mode_${mode.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        }

        if (academyState.activeMode == AcademyTestMode.KSEAB_MOCK_80) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("KSEAB 80-Mark Blueprint Pattern", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Physics (~28M) • Chemistry (~25M) • Biology (~27M) | Includes MCQs, 1M, 2M, 3M, 4M & 5M questions. Practice all chapter questions + rubric grader below.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        itemsIndexed(activeQuestions, key = { _, q -> q.id }) { idx, q ->
            val chosen = academyState.selectedAnswers[q.id]
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Question ${idx + 1}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        AssistChip(onClick = {}, label = { Text("${q.marks} Mark") })
                    }
                    Text(q.question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    q.options.forEachIndexed { optIdx, optionText ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = chosen == optIdx,
                                onClick = { onSelectMcqOption(q.id, optIdx) },
                                enabled = !academyState.mcqSubmitted
                            )
                            Text(optionText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (academyState.mcqSubmitted) {
                        val isCorrect = chosen == q.correctIndex
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isCorrect) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = (if (isCorrect) "Correct! " else "Incorrect. ") + q.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.fillMaxWidth().padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            if (!academyState.mcqSubmitted) {
                Button(
                    onClick = { onSubmitMcqTest(curriculumState.selectedChapterId, activeQuestions) },
                    enabled = activeQuestions.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("submit_academy_mcq")
                ) {
                    Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Objective Test (${activeQuestions.size} Qs)")
                }
            } else {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Score: ${academyState.mcqScore} / ${academyState.mcqMaxScore}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        OutlinedButton(onClick = { onSelectMode(academyState.activeMode) }) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retake")
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Descriptive & Numerical Rubric Evaluator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        descriptivePrompts.forEachIndexed { idx, p ->
                            FilterChip(
                                selected = idx == academyState.activeDescriptiveIndex,
                                onClick = { onSelectDescriptivePrompt(idx) },
                                label = { Text("${p.division.displayName} (${p.marks}M)") }
                            )
                        }
                    }

                    DivisionBadge(division = activeDescriptive.division)
                    Text(activeDescriptive.question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

                    OutlinedTextField(
                        value = academyState.descriptiveInput,
                        onValueChange = onUpdateDescriptiveInput,
                        label = { Text("Write your step-by-step board exam answer") },
                        placeholder = { Text("Include Given, Formula/Balanced Equation, SI Units, and Conclusion...") },
                        minLines = 4,
                        maxLines = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("descriptive_answer_input")
                    )

                    Button(
                        onClick = onEvaluateDescriptive,
                        enabled = !academyState.isEvaluating && academyState.descriptiveInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("evaluate_descriptive_button")
                    ) {
                        if (academyState.isEvaluating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Evaluating with Rubric...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Evaluate Answer (${activeDescriptive.marks} Marks)")
                        }
                    }

                    academyState.evaluationError?.let { err ->
                        Text(err, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }

                    academyState.evaluationResult?.let { res ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Practice Score: ${res.marksAwarded} / ${res.maxMarks}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(if (res.isAiEvaluated) "AI Rubric" else "Offline Rubric") }
                                    )
                                }
                                if (res.missingPoints.isNotEmpty()) {
                                    Text("Missing Key Points:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                                    res.missingPoints.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                                }
                                if (res.scientificErrors.isNotEmpty()) {
                                    Text("Scientific / Unit Corrections:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.error)
                                    res.scientificErrors.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                                }
                                Text("Suggestion: ${res.suggestedImprovement}", style = MaterialTheme.typography.bodySmall)
                                Text("Model Answer: ${res.modelAnswer}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                Text(
                                    text = "Note: Practice rubric guidance only — not an official KSEAB board score.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
