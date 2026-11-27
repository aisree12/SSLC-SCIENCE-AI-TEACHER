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
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
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
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ವಿಜ್ಞಾನ ಹಿಂದಿನ ಪ್ರಶ್ನೆಗಳ ಕೇಂದ್ರ" else "Science PYQ Intelligence Centre",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "2015 ರಿಂದ 2026 ರವರೆಗಿನ ಪ್ರಶ್ನೆಪತ್ರಿಕೆ ಸಂಗ್ರಹ ಲಭ್ಯವಿದೆ." else curriculumState.pyqDiscoveryStatus.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        OutlinedButton(
                            onClick = onRefreshPyqDiscovery,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh paper archive check", modifier = Modifier.size(18.dp))
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
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        FilterChip(
                            selected = filterBySelectedChapter,
                            onClick = { filterBySelectedChapter = !filterBySelectedChapter },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                            label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಆಯ್ದ ಅಧ್ಯಾಯ ಮಾತ್ರ" else "Only Selected Chapter", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("pyq_filter_chapter_chip")
                        )
                        if (curriculumState.selectedLanguage == StudyLanguage.ENGLISH) {
                        FilterChip(
                            selected = showAnalytics,
                            onClick = { showAnalytics = !showAnalytics },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                            leadingIcon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ವಿಶ್ಲೇಷಣೆ" else "Analytics & Predictions", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("pyq_analytics_chip")
                        )
                        }
                        FilterChip(
                            selected = showPaperArchive,
                            onClick = { showPaperArchive = !showPaperArchive },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                            label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಹಳೆಯ ಪ್ರಶ್ನೆಪತ್ರಿಕೆಗಳು" else "Past Paper PDFs", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("pyq_paper_archive_chip")
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(null to (if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಎಲ್ಲಾ" else "All"), 2026 to "2026", 2025 to "2025", 2024 to "2024", 2023 to "2023", 2022 to "2022", 2021 to "2021", 2020 to "2020", 2019 to "2019", 2018 to "2018", 2017 to "2017", 2016 to "2016", 2015 to "2015").forEach { (yr, label) ->
                            FilterChip(
                                selected = selectedYear == yr,
                                onClick = { selectedYear = yr },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                                label = { Text(label, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(null to (if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಎಲ್ಲಾ ಅಂಕಗಳು" else "All Marks"), 1 to "1M", 2 to "2M", 3 to "3M", 4 to "4M").forEach { (m, label) ->
                            FilterChip(
                                selected = selectedMarks == m,
                                onClick = { selectedMarks = m },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                                label = { Text(label, fontWeight = FontWeight.Bold) }
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
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                            label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಎಲ್ಲಾ ಪರೀಕ್ಷೆಗಳು" else "All Exams", fontWeight = FontWeight.Bold) }
                        )
                        PyqExamType.entries.forEach { et ->
                            FilterChip(
                                selected = selectedExamType == et,
                                onClick = { selectedExamType = if (selectedExamType == et) null else et },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                                label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) when (et) { PyqExamType.MAIN -> "ಮುಖ್ಯ ಪರೀಕ್ಷೆ"; PyqExamType.EXAM_1 -> "ಪರೀಕ್ಷೆ 1"; PyqExamType.EXAM_2 -> "ಪರೀಕ್ಷೆ 2"; PyqExamType.EXAM_3 -> "ಪರೀಕ್ಷೆ 3"; PyqExamType.SUPPLEMENTARY -> "ಪೂರಕ ಪರೀಕ್ಷೆ"; PyqExamType.PREPARATORY -> "ಪೂರ್ವಸಿದ್ಧತಾ ಪರೀಕ್ಷೆ"; PyqExamType.MODEL_PAPER -> "ಮಾದರಿ ಪ್ರಶ್ನೆಪತ್ರಿಕೆ" } else et.label, fontWeight = FontWeight.Bold) }
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
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                            label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಎಲ್ಲಾ ವಿಧಗಳು" else "All Types", fontWeight = FontWeight.Bold) }
                        )
                        QuestionType.entries.forEach { qt ->
                            FilterChip(
                                selected = selectedType == qt,
                                onClick = { selectedType = if (selectedType == qt) null else qt },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8), containerColor = Color(0xFFF1F5F9), labelColor = Color(0xFF1E293B)),
                                label = { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) when (qt) { QuestionType.MCQ -> "ಬಹು ಆಯ್ಕೆ"; QuestionType.DEFINITION -> "ವ್ಯಾಖ್ಯಾನ"; QuestionType.DIFFERENCE -> "ವ್ಯತ್ಯಾಸ"; QuestionType.EXPLAIN_WHY -> "ಕಾರಣ ವಿವರಿಸಿ"; QuestionType.NUMERICAL -> "ಸಮಸ್ಯೆ"; QuestionType.DIAGRAM -> "ಚಿತ್ರ"; QuestionType.CHEMICAL_EQUATION -> "ರಾಸಾಯನಿಕ ಸಮೀಕರಣ"; QuestionType.DESCRIPTIVE -> "ವಿವರಣಾತ್ಮಕ" } else qt.label, fontWeight = FontWeight.Bold) }
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
                        Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಕರ್ನಾಟಕ SSLC ವಿಜ್ಞಾನ ಪ್ರಶ್ನೆಪತ್ರಿಕೆ ಸಂಗ್ರಹ" else "Karnataka SSLC Science Paper Archive", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಪ್ರತಿ ವರ್ಷದ ಹಿಂದಿನ ಪ್ರಶ್ನೆಪತ್ರಿಕೆಗಳನ್ನು ಇಲ್ಲಿ ತೆರೆಯಬಹುದು. ಸರಿಯಾದ ವರ್ಷ ಮತ್ತು ಪರೀಕ್ಷಾ ಆವೃತ್ತಿಯನ್ನು ಆಯ್ಕೆಮಾಡಿ." else "Archive indexes and mirrors are clearly labelled per collection. 2026 includes KSEAB model answers in English and Kannada. Older mirror PDFs are not official answer keys; check the paper code, year and medium before using them.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            items(PyqRepository.paperArchive, key = { it.id }) { entry ->
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "${entry.title.take(4)} SSLC ವಿಜ್ಞಾನ ಪ್ರಶ್ನೆಪತ್ರಿಕೆ" else entry.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ವರ್ಷ ${entry.title.take(4)} • ಹಿಂದಿನ ಪ್ರಶ್ನೆಪತ್ರಿಕೆ" else entry.subtitle, style = MaterialTheme.typography.bodySmall)
                        if (curriculumState.selectedLanguage == StudyLanguage.ENGLISH) Text(entry.sourceNote, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(entry.paperArchiveUrl)))
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಪ್ರಶ್ನೆಪತ್ರಿಕೆ ತೆರೆಯಿರಿ" else "Open Papers") }
                            if (entry.answerKeyArchiveUrl != null) {
                                OutlinedButton(
                                    onClick = {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(entry.answerKeyArchiveUrl)))
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text(if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "ಉತ್ತರ ಕೀ ತೆರೆಯಿರಿ" else "Open Keys") }
                            }
                        }
                    }
                }
            }
        }

        if (showAnalytics && curriculumState.selectedLanguage == StudyLanguage.ENGLISH) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Question-bank Patterns (Limited Sample)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Question bank: ${analytics.totalQuestions} practice questions • Source-verified official keys: ${analytics.verifiedOfficialKeyCount}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text("Diagram Questions in This Bank:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        analytics.topDiagrams.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        Text("Chemical-equation and Numerical Questions:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        analytics.topReactions.take(2).forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        analytics.topNumericals.take(2).forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Possible Revision Focus (Not Guaranteed — Small Question-Bank Sample):",
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (language == StudyLanguage.KANNADA) Text(pyq.division.kannadaName, style = MaterialTheme.typography.labelMedium) else DivisionBadge(division = pyq.division)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(onClick = {}, label = { Text(if (language == StudyLanguage.KANNADA) "${pyq.year} • ಪರೀಕ್ಷೆ" else "${pyq.year} • ${pyq.examType.label}", fontWeight = FontWeight.Bold) })
                    AssistChip(onClick = {}, label = { Text("${pyq.marks}M", fontWeight = FontWeight.Bold) })
                    if (pyq.flaggedForReview) AssistChip(onClick = {}, label = { Text(if (language == StudyLanguage.KANNADA) "ಅಭ್ಯಾಸ ಪ್ರಶ್ನೆ" else "Practice • Source pending", fontWeight = FontWeight.Bold) })
                }
            }

            Text(
                text = if (language == StudyLanguage.KANNADA) "ಪ್ರಶ್ನೆ ${pyq.questionNumber} • ${SyllabusRepository.getChapterById(pyq.chapterId).titleKn}" else "${pyq.questionNumber} • ${pyq.topicName} (${pyq.questionType.label})",
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


            if (language == StudyLanguage.ENGLISH && pyq.hasOfficialKey && !pyq.officialKeyAnswer.isNullOrBlank()) {
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
                        text = if (language == StudyLanguage.KANNADA) "ಈ ಪ್ರಶ್ನೆಯ ಉತ್ತರಕ್ಕಾಗಿ AI ಶಿಕ್ಷಕರನ್ನು ಕೇಳಿ." else if (pyq.flaggedForReview) "Practice solution not yet matched to an official source. Use AI Teacher Solution for further practice." else "Official Key not published (${pyq.officialSource}). Use AI Teacher Solution for practice.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (language == StudyLanguage.ENGLISH && pyq.hasOfficialKey) {
                    OutlinedButton(
                        onClick = { showOfficialKey = !showOfficialKey },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("toggle_official_key_${pyq.id}")
                    ) {
                        Text(if (language == StudyLanguage.KANNADA) { if (showOfficialKey) "ಉತ್ತರ ಮರೆಮಾಡಿ" else "ಉತ್ತರ ನೋಡಿ" } else if (showOfficialKey) "Hide Answer" else if (pyq.flaggedForReview) "Practice Answer" else "Official Key")
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
                    Text(if (language == StudyLanguage.KANNADA) "AI ಪರಿಹಾರ" else "AI Solution")
                }
                OutlinedButton(
                    onClick = onOpenOfficialSource,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open source paper reference", modifier = Modifier.size(18.dp))
                    Text(if (language == StudyLanguage.KANNADA) "ಪ್ರಶ್ನೆಪತ್ರಿಕೆ" else "KSEAB")
                }
            }
        }
    }
}
