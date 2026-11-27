package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.curriculum.SyllabusRepository
import com.example.data.pyq.PyqRepository
import com.example.model.PyqExamType
import com.example.model.PyqQuestion
import com.example.model.QuestionType
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.ui.components.DivisionBadge
import com.example.ui.components.UnifiedCurriculumControls
import com.example.viewmodel.CurriculumUiState

@Composable
fun PyqCentreScreen(
    curriculumState: CurriculumUiState,
    onSelectSubject: (SubjectFilter) -> Unit,
    onSelectChapter: (String) -> Unit,
    onSelectTopic: (String) -> Unit,
    onSelectLanguage: (StudyLanguage) -> Unit,
    onRefreshPyqDiscovery: () -> Unit,
    onAskAiForPyqSolution: (chapterId: String, prompt: String) -> Unit
) {
    val context = LocalContext.current
    var filterBySelectedChapter by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var selectedExamType by remember { mutableStateOf<PyqExamType?>(null) }
    var selectedMarks by remember { mutableStateOf<Int?>(null) }
    var selectedType by remember { mutableStateOf<QuestionType?>(null) }
    var showAnalytics by remember { mutableStateOf(false) }
    var showPaperArchive by remember { mutableStateOf(false) }

    val filteredPyqs = remember(
        curriculumState.selectedSubject,
        curriculumState.selectedChapterId,
        filterBySelectedChapter,
        selectedYear,
        selectedExamType,
        selectedMarks,
        selectedType
    ) {
        PyqRepository.allQuestions.filter { q ->
            val subjectOk = curriculumState.selectedSubject.division?.let { q.division == it } ?: true
            val chapterOk = !filterBySelectedChapter || q.chapterId == curriculumState.selectedChapterId
            val yearOk = selectedYear == null || q.year == selectedYear
            val examOk = selectedExamType == null || q.examType == selectedExamType
            val marksOk = selectedMarks == null || q.marks == selectedMarks
            val typeOk = selectedType == null || q.questionType == selectedType
            subjectOk && chapterOk && yearOk && examOk && marksOk && typeOk
        }
    }

    val analytics = remember(filteredPyqs) {
        PyqRepository.computeAnalytics(filteredPyqs.ifEmpty { PyqRepository.allQuestions })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("pyq_centre_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Science PYQ Intelligence Centre",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = curriculumState.pyqDiscoveryStatus.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        OutlinedButton(
                            onClick = onRefreshPyqDiscovery,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh KSEAB check", modifier = Modifier.size(18.dp))
                        }
                    }

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
                        FilterChip(
                            selected = filterBySelectedChapter,
                            onClick = { filterBySelectedChapter = !filterBySelectedChapter },
                            label = { Text("Only Selected Chapter") },
                            modifier = Modifier.testTag("pyq_filter_chapter_chip")
                        )
                        FilterChip(
                            selected = showAnalytics,
                            onClick = { showAnalytics = !showAnalytics },
                            leadingIcon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text("Analytics & Predictions") },
                            modifier = Modifier.testTag("pyq_analytics_chip")
                        )
                        FilterChip(
                            selected = showPaperArchive,
                            onClick = { showPaperArchive = !showPaperArchive },
                            label = { Text("Past Paper PDFs") },
                            modifier = Modifier.testTag("pyq_paper_archive_chip")
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(null to "All Years", 2026 to "2026", 2025 to "2025", 2024 to "2024", 2023 to "2023", 2022 to "2022").forEach { (yr, label) ->
                            FilterChip(
                                selected = selectedYear == yr,
                                onClick = { selectedYear = yr },
                                label = { Text(label) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(null to "All Marks", 1 to "1M", 2 to "2M", 3 to "3M", 4 to "4M").forEach { (m, label) ->
                            FilterChip(
                                selected = selectedMarks == m,
                                onClick = { selectedMarks = m },
                                label = { Text(label) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedExamType == null,
                            onClick = { selectedExamType = null },
                            label = { Text("All Exams") }
                        )
                        PyqExamType.entries.forEach { et ->
                            FilterChip(
                                selected = selectedExamType == et,
                                onClick = { selectedExamType = if (selectedExamType == et) null else et },
                                label = { Text(et.label) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedType == null,
                            onClick = { selectedType = null },
                            label = { Text("All Types") }
                        )
                        QuestionType.entries.forEach { qt ->
                            FilterChip(
                                selected = selectedType == qt,
                                onClick = { selectedType = if (selectedType == qt) null else qt },
                                label = { Text(qt.label) }
                            )
                        }
                    }
                }
            }
        }

        if (showPaperArchive) {
            item {
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("KSEAB Science Paper Archive", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Archive indexes and mirrors are clearly labelled per collection. 2026 includes KSEAB model answers in English and Kannada. Older mirror PDFs are not official answer keys; check the paper code, year and medium before using them.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            items(PyqRepository.paperArchive, key = { it.id }) { entry ->
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Text(entry.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(entry.subtitle, style = MaterialTheme.typography.bodySmall)
                        Text(entry.sourceNote, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(entry.paperArchiveUrl)))
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("Open Papers") }
                            if (entry.answerKeyArchiveUrl != null) {
                                OutlinedButton(
                                    onClick = {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(entry.answerKeyArchiveUrl)))
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Open Keys") }
                            }
                        }
                    }
                }
            }
        }

        if (showAnalytics) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("PYQ Trends & Study Focus", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Question bank: ${analytics.totalQuestions} practice questions • Source-verified official keys: ${analytics.verifiedOfficialKeyCount}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text("Top Repeated Diagrams:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        analytics.topDiagrams.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        Text("Top Repeated Reactions & Numericals:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        analytics.topReactions.take(2).forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        analytics.topNumericals.take(2).forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Exam Focus Predictions (Not Guaranteed — Statistical Trends Only):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                analytics.probablePredictionTopics.forEach {
                                    Text("• $it", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (filteredPyqs.isEmpty()) {
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("No PYQs matched your selected filters.", style = MaterialTheme.typography.titleSmall)
                        Button(onClick = {
                            filterBySelectedChapter = false
                            selectedYear = null
                            selectedExamType = null
                            selectedMarks = null
                            selectedType = null
                        }) {
                            Text("Reset PYQ Filters")
                        }
                    }
                }
            }
        } else {
            items(filteredPyqs, key = { it.id }) { pyq ->
                PyqCardItem(
                    pyq = pyq,
                    language = curriculumState.selectedLanguage,
                    onAskAiSolution = {
                        onAskAiForPyqSolution(
                            pyq.chapterId,
                            "Solve this ${pyq.year} ${pyq.examType.label} (${pyq.marks} Marks) KSEAB SSLC Science question step-by-step: ${pyq.questionText}"
                        )
                    },
                    onOpenOfficialSource = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pyq.paperReferenceUrl ?: pyq.officialPdfUrl.ifBlank { "https://kseab.karnataka.gov.in/" })))
                    }
                )
            }
        }
    }
}

@Composable
private fun PyqCardItem(
    pyq: PyqQuestion,
    language: StudyLanguage,
    onAskAiSolution: () -> Unit,
    onOpenOfficialSource: () -> Unit
) {
    var showOfficialKey by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DivisionBadge(division = pyq.division)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(onClick = {}, label = { Text("${pyq.year} • ${pyq.examType.label}") })
                    AssistChip(onClick = {}, label = { Text("${pyq.marks}M") })
                    if (pyq.flaggedForReview) AssistChip(onClick = {}, label = { Text("Practice • Source pending") })
                }
            }

            Text(
                text = "${pyq.questionNumber} • ${pyq.topicName} (${pyq.questionType.label})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (language == StudyLanguage.KANNADA) pyq.kannadaQuestionText else pyq.questionText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            if (pyq.paperReferenceUrl != null) {
                Text(
                    "Paper reference: third-party mirror; original KSEAB PDF verification pending.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (language == StudyLanguage.BILINGUAL) {
                Text(
                    text = pyq.kannadaQuestionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (pyq.hasOfficialKey && !pyq.officialKeyAnswer.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (pyq.flaggedForReview) "Practice answer — official source verification pending" else "Verified Official KSEAB Key Available",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (showOfficialKey) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(pyq.officialKeyAnswer, style = MaterialTheme.typography.bodySmall)
                            Text(if (pyq.flaggedForReview) "Reference label (not yet verified): ${pyq.officialSource}" else "Source: ${pyq.officialSource}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (pyq.flaggedForReview) "Practice solution not yet matched to an official source. Use AI Teacher Solution for further practice." else "Official Key not published (${pyq.officialSource}). Use AI Teacher Solution for practice.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (pyq.hasOfficialKey) {
                    OutlinedButton(
                        onClick = { showOfficialKey = !showOfficialKey },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("toggle_official_key_${pyq.id}")
                    ) {
                        Text(if (showOfficialKey) "Hide Answer" else if (pyq.flaggedForReview) "Practice Answer" else "Official Key")
                    }
                }
                Button(
                    onClick = onAskAiSolution,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("ai_solve_pyq_${pyq.id}")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AI Solution")
                }
                OutlinedButton(
                    onClick = onOpenOfficialSource,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open source paper reference", modifier = Modifier.size(18.dp))
                    Text("KSEAB")
                }
            }
        }
    }
}
