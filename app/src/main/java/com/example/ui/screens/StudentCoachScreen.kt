package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
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
import com.example.model.ChapterProgress
import com.example.model.ExamAttempt
import com.example.model.SavedStudyNote
import com.example.model.ScienceDivision
import com.example.ui.components.DivisionBadge
import com.example.ui.components.divisionAccentColor
import com.example.viewmodel.CurriculumUiState
import kotlin.math.roundToInt

@Composable
fun StudentCoachScreen(
    userDisplayName: String,
    userEmail: String,
    curriculumState: CurriculumUiState,
    chapterProgressMap: Map<String, ChapterProgress>,
    examAttempts: List<ExamAttempt>,
    savedNotes: List<SavedStudyNote>,
    onUpdateDailyTarget: (Int) -> Unit,
    onOpenChapterInStudyCentre: (String) -> Unit,
    onSignOutClick: () -> Unit
) {
    val allChapters = SyllabusRepository.chapters
    val overallPercent = remember(chapterProgressMap) {
        if (allChapters.isEmpty()) 0
        else allChapters.sumOf { (chapterProgressMap[it.id]?.masteryPercent ?: 0L).toInt() } / allChapters.size
    }

    val divisionStats = remember(chapterProgressMap) {
        ScienceDivision.entries.associateWith { div ->
            val divChapters = allChapters.filter { it.division == div }
            if (divChapters.isEmpty()) 0
            else divChapters.sumOf { (chapterProgressMap[it.id]?.masteryPercent ?: 0L).toInt() } / divChapters.size
        }
    }

    val weakChapters = remember(chapterProgressMap) {
        allChapters.filter { (chapterProgressMap[it.id]?.masteryPercent ?: 0L) < 50L }.take(4)
    }
    val strongChapters = remember(chapterProgressMap) {
        allChapters.filter { (chapterProgressMap[it.id]?.masteryPercent ?: 0L) >= 70L }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("student_coach_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Student profile",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(userDisplayName.ifBlank { "SSLC Student" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(userEmail.ifBlank { "Google Account Connected" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        OutlinedButton(
                            onClick = onSignOutClick,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("sign_out_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign out", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sign Out")
                        }
                    }

                    Text("Overall Syllabus Completion: $overallPercent%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(
                        progress = { (overallPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ScienceDivision.entries.forEach { div ->
                            val pct = divisionStats[div] ?: 0
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(div.displayName, style = MaterialTheme.typography.labelMedium, color = divisionAccentColor(div), fontWeight = FontWeight.Bold)
                                    Text("$pct%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Daily Science Study Target: ${curriculumState.dailyTargetMinutes} min", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = curriculumState.dailyTargetMinutes.toFloat(),
                        onValueChange = { onUpdateDailyTarget((it / 5f).roundToInt() * 5) },
                        valueRange = 15f..120f,
                        steps = 20,
                        modifier = Modifier.testTag("daily_target_slider")
                    )
                    Text(
                        text = "Recommended split: 15 min Textbook/Notes + 15 min Interactive Lab/Diagrams + ${((curriculumState.dailyTargetMinutes - 30).coerceAtLeast(10))} min KSEAB PYQs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Personalized Study Plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (weakChapters.isNotEmpty()) {
                        Text("Focus Chapters (Needs Revision):", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        weakChapters.forEach { ch ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Ch ${ch.chapterNumber}: ${ch.titleEn}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                    Text("${chapterProgressMap[ch.id]?.masteryPercent ?: 0}% completed", style = MaterialTheme.typography.bodySmall)
                                }
                                OutlinedButton(onClick = { onOpenChapterInStudyCentre(ch.id) }) {
                                    Text("Revise")
                                }
                            }
                        }
                    }
                    if (strongChapters.isNotEmpty()) {
                        Text(
                            text = "Strong Chapters (>=70%): ${strongChapters.joinToString { "Ch ${it.chapterNumber}" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        if (savedNotes.isNotEmpty()) {
            item {
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Offline & Cloud Saved Notes (${savedNotes.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        savedNotes.take(5).forEach { note ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                                    Text(note.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                    Text(note.content.take(160) + if (note.content.length > 160) "..." else "", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (examAttempts.isNotEmpty()) {
            item {
                Text("Recent Exam Academy Attempts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(examAttempts.take(8), key = { it.id }) { attempt ->
                Card(shape = RoundedCornerShape(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(attempt.feedbackSummary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text("${attempt.examType} • ${attempt.subject}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        AssistChip(onClick = {}, label = { Text("${attempt.score}/${attempt.maxScore}") })
                    }
                }
            }
        }

        item {
            Text("All 13 Chapters Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(allChapters, key = { it.id }) { ch ->
            val pct = (chapterProgressMap[ch.id]?.masteryPercent ?: 0L).toInt()
            Card(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DivisionBadge(division = ch.division)
                        Text("$pct%", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                    Text("Ch ${ch.chapterNumber}: ${ch.titleEn}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(
                        progress = { (pct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = { onOpenChapterInStudyCentre(ch.id) },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("Open in Study Centre")
                    }
                }
            }
        }
    }
}
