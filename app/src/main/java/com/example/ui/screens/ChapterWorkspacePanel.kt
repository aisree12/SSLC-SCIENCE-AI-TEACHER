package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lab.SimulationAndDiagramCatalog
import com.example.data.pyq.PyqRepository
import com.example.data.video.VideoRepository
import com.example.model.ChapterProgress
import com.example.model.PyqQuestion
import com.example.model.ScienceVideo
import com.example.model.StudyLanguage
import com.example.model.SyllabusChapter
import com.example.ui.components.DivisionBadge
import com.example.ui.components.VideoResourceCard

@Composable
fun ChapterWorkspacePanel(
    chapter: SyllabusChapter,
    selectedTopicId: String,
    language: StudyLanguage,
    chapterProgress: ChapterProgress?,
    onToggleTopicCompleted: (SyllabusChapter, String) -> Unit,
    onSaveNotesOffline: (SyllabusChapter) -> Unit,
    onAskAiTeacher: (chapterId: String, prefill: String) -> Unit,
    onOpenLabForChapter: (chapterId: String) -> Unit,
    onOpenPyqTab: () -> Unit,
    onWatchVideo: (ScienceVideo) -> Unit,
    textbookActionLabel: String,
    onOpenTextbook: () -> Unit
) {
    var activeTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = if (language == StudyLanguage.KANNADA) listOf("ವಿಷಯಗಳು", "ಟಿಪ್ಪಣಿಗಳು", "ವೀಡಿಯೊಗಳು", "ಹಿಂದಿನ ಪ್ರಶ್ನೆಗಳು", "ಪ್ರಯೋಗಗಳು", "ಪರೀಕ್ಷೆ") else listOf("Topics", "Notes & Formulas", "Videos", "PYQs", "Lab & Diagrams", "Quick Quiz")
    val completedIds = chapterProgress?.completedTopicIds?.toSet() ?: emptySet()

    Column(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (language == StudyLanguage.KANNADA) Text(chapter.division.kannadaName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold) else DivisionBadge(division = chapter.division)
                    Text(
                        text = if (language == StudyLanguage.KANNADA) "ಭಾಗ ${chapter.textbookPart} • ಅಧ್ಯಾಯ ${chapter.chapterNumber}" else "Part ${chapter.textbookPart} • Ch ${chapter.chapterNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = if (language == StudyLanguage.KANNADA) chapter.titleKn else chapter.titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (language == StudyLanguage.ENGLISH) {
                    Text(chapter.introduction, style = MaterialTheme.typography.bodyMedium)
                    Text("Source: ${chapter.textbookSourceRef}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }

                Button(
                    onClick = onOpenTextbook,
                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF0F766E)),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 5.dp),
                    modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                ) {
                    Text(textbookActionLabel, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onSaveNotesOffline(chapter) },
                        colors = OutlinedButtonDefaults.outlinedButtonColors(contentColor = androidx.compose.ui.graphics.Color(0xFF6D28D9)),
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("save_notes_offline_button")
                    ) {
                        Icon(Icons.Default.BookmarkAdded, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == StudyLanguage.KANNADA) "ಟಿಪ್ಪಣಿಗಳನ್ನು ಉಳಿಸಿ" else "Save Notes", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            onAskAiTeacher(
                                chapter.id,
                                "Explain Chapter ${chapter.chapterNumber} (${chapter.titleEn}) in ${language.label}"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF2563EB)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("ask_ai_chapter_button")
                    ) {
                        Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == StudyLanguage.KANNADA) "AI ಶಿಕ್ಷಕರನ್ನು ಕೇಳಿ" else "Ask AI Teacher", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val tabAccentColors = listOf(
        androidx.compose.ui.graphics.Color(0xFF2563EB),
        androidx.compose.ui.graphics.Color(0xFF7C3AED),
        androidx.compose.ui.graphics.Color(0xFF0891B2),
        androidx.compose.ui.graphics.Color(0xFFDB2777),
        androidx.compose.ui.graphics.Color(0xFF16A34A),
        androidx.compose.ui.graphics.Color(0xFFD97706)
    )
    val animatedTabAccent by animateColorAsState(
        targetValue = tabAccentColors[activeTabIndex],
        label = "workspaceTabAccent"
    )

    ScrollableTabRow(
            selectedTabIndex = activeTabIndex,
            edgePadding = 0.dp
        ) {
            tabTitles.forEachIndexed { idx, label ->
                Tab(
                    selected = activeTabIndex == idx,
                    onClick = { activeTabIndex = idx },
                    selectedContentColor = animatedTabAccent,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1) },
                    modifier = Modifier.testTag("workspace_tab_$idx")
                )
            }
        }

        when (activeTabIndex) {
            0 -> TopicsSection(
                chapter = chapter,
                selectedTopicId = selectedTopicId,
                language = language,
                completedIds = completedIds,
                onToggleCompleted = { topicId -> onToggleTopicCompleted(chapter, topicId) },
                onAskAiTopic = { topicTitle ->
                    onAskAiTeacher(chapter.id, "Teach me '$topicTitle' from ${chapter.titleEn} clearly for SSLC board exam.")
                }
            )
            1 -> NotesFormulasSection(chapter = chapter, language = language)
            2 -> VideosSection(chapterId = chapter.id, onWatchVideo = onWatchVideo)
            3 -> ChapterPyqsSection(chapterId = chapter.id, language = language, onOpenPyqTab = onOpenPyqTab)
            4 -> ChapterLabPreviewSection(
                chapter = chapter,
                onOpenLabForChapter = { onOpenLabForChapter(chapter.id) }
            )
            5 -> ChapterQuickQuizSection(chapter = chapter)
        }
    }
}

@Composable
private fun TopicsSection(
    chapter: SyllabusChapter,
    selectedTopicId: String,
    language: StudyLanguage,
    completedIds: Set<String>,
    onToggleCompleted: (String) -> Unit,
    onAskAiTopic: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        chapter.topics.forEach { topic ->
            val isCompleted = completedIds.contains(topic.id)
            val isFocused = topic.id == selectedTopicId
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFocused) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == StudyLanguage.KANNADA) topic.kannadaTitle else topic.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { onToggleCompleted(topic.id) },
                            modifier = Modifier.testTag("toggle_topic_${topic.id}")
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = if (isCompleted) "Mark incomplete" else "Mark completed",
                                tint = if (isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = if (language == StudyLanguage.KANNADA) topic.kannadaSummary else topic.simpleExplanation,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (language == StudyLanguage.ENGLISH && topic.observations.isNotEmpty()) {
                        Text(
                            text = "Key Observations: ${topic.observations.joinToString(" • ")}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (language == StudyLanguage.ENGLISH && topic.dailyLifeApplications.isNotEmpty()) {
                        Text(
                            text = "Daily Life: ${topic.dailyLifeApplications.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    OutlinedButton(
                        onClick = { onAskAiTopic(topic.title) },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text(if (language == StudyLanguage.KANNADA) "ಈ ವಿಷಯದ ಬಗ್ಗೆ AI ಶಿಕ್ಷಕರನ್ನು ಕೇಳಿ" else "Ask AI Teacher about this topic")
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesFormulasSection(
    chapter: SyllabusChapter,
    language: StudyLanguage
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (chapter.definitions.isNotEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (language == StudyLanguage.KANNADA) "ಪ್ರಮುಖ ವ್ಯಾಖ್ಯಾನಗಳು" else "Key Definitions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    chapter.definitions.forEach { d ->
                        Text(
                            text = if (language == StudyLanguage.KANNADA) d.kannadaTerm else d.term,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == StudyLanguage.KANNADA) d.kannadaDefinition else d.definition,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (language == StudyLanguage.ENGLISH) Text(text = d.textbookRef, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        if (chapter.formulas.isNotEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (language == StudyLanguage.KANNADA) "ಸೂತ್ರಗಳು ಮತ್ತು SI ಏಕಮಾನಗಳು" else "Formulas & SI Units", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    chapter.formulas.forEach { f ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                                if (language == StudyLanguage.ENGLISH) Text(f.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Text(f.formula, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                if (language == StudyLanguage.ENGLISH) Text("${f.variables} • SI Unit: ${f.siUnit}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        if (chapter.reactions.isNotEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (language == StudyLanguage.KANNADA) "ಸಮತೋಲಿತ ರಾಸಾಯನಿಕ ಸಮೀಕರಣಗಳು" else "Balanced Chemical Equations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    chapter.reactions.forEach { r ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                                if (language == StudyLanguage.ENGLISH) Text("${r.title} (${r.reactionType})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Text(r.balancedEquation, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                                if (language == StudyLanguage.ENGLISH) Text("Observation: ${r.observation}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        if (chapter.division.name == "CHEMISTRY" && chapter.reactions.isNotEmpty()) {
            ReactionBalancerWorkspace(language = language)
        }

        if (language == StudyLanguage.ENGLISH && chapter.activities.isNotEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Textbook Practical Activities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    chapter.activities.forEach { act ->
                        Text("${act.activityNumber}: ${act.title}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text("Aim: ${act.aim}", style = MaterialTheme.typography.bodySmall)
                        Text("Observation: ${act.observation}", style = MaterialTheme.typography.bodySmall)
                        Text("Inference: ${act.inference}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        if (language == StudyLanguage.ENGLISH) Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Board Exam Quick Revision Notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                chapter.revisionNotes.forEach { note ->
                    Text("• $note", style = MaterialTheme.typography.bodyMedium)
                }
                if (chapter.faqs.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Important FAQs", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    chapter.faqs.forEach { faq ->
                        Text("Q [${faq.marksHint}]: ${faq.question}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("A: ${faq.answer}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun VideosSection(chapterId: String, onWatchVideo: (ScienceVideo) -> Unit) {
    val videos = remember(chapterId) { VideoRepository.videosForChapter(chapterId) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (videos.isEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Text(
                    "No indexed videos for this chapter yet.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            videos.forEach { vid ->
                VideoResourceCard(video = vid, onWatchClick = onWatchVideo)
            }
        }
    }
}

@Composable
private fun ChapterPyqsSection(
    chapterId: String,
    language: StudyLanguage,
    onOpenPyqTab: () -> Unit
) {
    val items: List<PyqQuestion> = remember(chapterId) {
        PyqRepository.allQuestions.filter { it.chapterId == chapterId }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (items.isEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Explore full KSEAB PYQs across all years in the PYQs tab.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onOpenPyqTab) { Text("Open PYQ Intelligence Centre") }
                }
            }
        } else {
            items.forEach { pyq ->
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, label = { Text("${pyq.year} ${pyq.examType.label}") })
                            AssistChip(onClick = {}, label = { Text("${pyq.marks} Marks") })
                        }
                        Text(
                            text = if (language == StudyLanguage.KANNADA) pyq.kannadaQuestionText else pyq.questionText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (pyq.hasOfficialKey) "Official Key: ${pyq.officialKeyAnswer}" else "Official Key not published — solve via AI Teacher in PYQs tab.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            OutlinedButton(
                onClick = onOpenPyqTab,
                modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
            ) {
                Text(if (language == StudyLanguage.KANNADA) "ಪೂರ್ಣ ಹಿಂದಿನ ಪ್ರಶ್ನೆಗಳ ಕೇಂದ್ರ ತೆರೆಯಿರಿ" else "Open Full PYQ Intelligence Centre")
            }
        }
    }
}

@Composable
private fun ChapterLabPreviewSection(
    chapter: SyllabusChapter,
    onOpenLabForChapter: () -> Unit
) {
    val sims = remember(chapter.id) {
        SimulationAndDiagramCatalog.simulations.filter { it.chapterId == chapter.id }
    }
    val diags = remember(chapter.id) {
        SimulationAndDiagramCatalog.diagrams.filter { it.chapterId == chapter.id }
    }
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Interactive Simulations & Diagram Studio",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${sims.size} Interactive Simulation(s) • ${diags.size} Labelled Diagram(s) ready for Chapter ${chapter.chapterNumber}.",
                style = MaterialTheme.typography.bodyMedium
            )
            sims.forEach { s ->
                Text("• Lab: ${s.titleEn}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            diags.forEach { d ->
                Text("• Diagram: ${d.titleEn}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Button(
                onClick = onOpenLabForChapter,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("launch_lab_from_chapter")
            ) {
                Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Launch Science Lab & Diagram Studio")
            }
        }
    }
}

@Composable
private fun ChapterQuickQuizSection(chapter: SyllabusChapter) {
    val selected = remember(chapter.id) { mutableStateMapOf<String, Int>() }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        chapter.quizQuestions.forEachIndexed { idx, q ->
            val chosen = selected[q.id]
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Q${idx + 1}. ${q.question}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    q.options.forEachIndexed { optIdx, opt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = chosen == optIdx,
                                onClick = { selected[q.id] = optIdx }
                            )
                            Text(opt, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (chosen != null) {
                        val correct = chosen == q.correctIndex
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (correct) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = (if (correct) "Correct! " else "Review: ") + q.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
