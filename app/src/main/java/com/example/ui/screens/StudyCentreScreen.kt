package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.curriculum.SyllabusRepository
import com.example.data.textbook.TextbookRepository
import com.example.model.ChapterProgress
import com.example.model.DownloadStatus
import com.example.model.OfficialTextbook
import com.example.model.ScienceVideo
import com.example.model.StudyLanguage
import com.example.model.TextbookMedium
import com.example.model.SubjectFilter
import com.example.model.SyllabusChapter
import com.example.ui.components.OfflinePdfReaderDialog
import com.example.ui.components.UnifiedCurriculumControls
import com.example.ui.components.YouTubePlayerDialog
import com.example.viewmodel.CurriculumUiState

@Composable
fun StudyCentreScreen(
    uiState: CurriculumUiState,
    textbooks: List<OfficialTextbook>,
    chapterProgressMap: Map<String, ChapterProgress>,
    onSelectSubject: (SubjectFilter) -> Unit,
    onSelectChapter: (String) -> Unit,
    onSelectTopic: (String) -> Unit,
    onSelectLanguage: (StudyLanguage) -> Unit,
    onUpdateSearch: (String) -> Unit,
    onResetSearch: () -> Unit,
    onToggleTopicCompleted: (SyllabusChapter, String) -> Unit,
    onSaveChapterNotesOffline: (SyllabusChapter) -> Unit,
    onDownloadBook: (String) -> Unit,
    onUpdateBookReading: (bookId: String, page: Int, bookmarks: List<Int>) -> Unit,
    onRefreshVerification: () -> Unit,
    onAskAiTeacher: (chapterId: String, prefill: String) -> Unit,
    onOpenLabForChapter: (chapterId: String) -> Unit,
    onOpenPyqTab: () -> Unit,
    onDismissBanner: () -> Unit
) {
    val context = LocalContext.current
    var showVerificationDialog by remember { mutableStateOf(false) }
    var activeBookForReader by remember { mutableStateOf<OfficialTextbook?>(null) }
    var activeVideo by remember { mutableStateOf<ScienceVideo?>(null) }

    val availableChapters = uiState.availableChapters
    val currentChapter = uiState.selectedChapter
    val chapterBook = textbooks.firstOrNull { book ->
        book.medium == (if (uiState.selectedLanguage == StudyLanguage.KANNADA) TextbookMedium.KANNADA else TextbookMedium.ENGLISH) && book.part == currentChapter.textbookPart
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("study_centre_screen"),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.science_hero_banner_1795782260119),
                    contentDescription = "SSLC Science Study Centre Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xE60F2A4A), Color(0x6600796B))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.verificationInfo.liveCheckSucceeded) Icons.Default.Verified else Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = Color(0xFF99F6E4),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಕರ್ನಾಟಕ SSLC ವಿಜ್ಞಾನ • 13 ಅಧ್ಯಾಯಗಳು" else "Karnataka SSLC Science • 13 chapters",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF99F6E4)
                            )
                        }
                        Text(
                            text = if (uiState.selectedLanguage == StudyLanguage.KANNADA) "SSLC ವಿಜ್ಞಾನ ಅಧ್ಯಯನ ಕೇಂದ್ರ" else "SSLC Science Study Centre",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಅಧ್ಯಯನಕ್ಕಾಗಿ ಅಧ್ಯಾಯಗಳನ್ನು ಆಯ್ಕೆಮಾಡಿ" else uiState.verificationInfo.lastVerifiedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedSubject == SubjectFilter.ALL,
                    onClick = { onSelectSubject(SubjectFilter.ALL) },
                    label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಎಲ್ಲ ವಿಷಯಗಳು" else "All") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8))
                )
                FilterChip(
                    selected = uiState.selectedSubject == SubjectFilter.PHYSICS,
                    onClick = { onSelectSubject(SubjectFilter.PHYSICS) },
                    label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಭೌತಶಾಸ್ತ್ರ" else "Physics") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDBEAFE), selectedLabelColor = Color(0xFF1D4ED8))
                )
                FilterChip(
                    selected = uiState.selectedSubject == SubjectFilter.CHEMISTRY,
                    onClick = { onSelectSubject(SubjectFilter.CHEMISTRY) },
                    label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ರಸಾಯನಶಾಸ್ತ್ರ" else "Chemistry") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFCE7F3), selectedLabelColor = Color(0xFFBE185D))
                )
                FilterChip(
                    selected = uiState.selectedSubject == SubjectFilter.BIOLOGY,
                    onClick = { onSelectSubject(SubjectFilter.BIOLOGY) },
                    label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಜೀವಶಾಸ್ತ್ರ" else "Biology") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFDCFCE7), selectedLabelColor = Color(0xFF15803D))
                )
            }
        }

        if (!uiState.statusBannerMessage.isNullOrBlank() || !uiState.syncErrorMessage.isNullOrBlank()) {
            item {
                val msg = uiState.statusBannerMessage ?: uiState.syncErrorMessage.orEmpty()
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (uiState.statusBannerMessage != null) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(msg, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        IconButton(onClick = onDismissBanner) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss")
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onOpenLabForChapter(currentChapter.id) },
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("open_lab_studio_button")
                ) {
                    Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಪ್ರಯೋಗಾಲಯ ಮತ್ತು ಚಿತ್ರಗಳು" else "Lab & Diagrams")
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onUpdateSearch,
                        singleLine = true,
                        label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಅಧ್ಯಾಯಗಳು, ನಿಯಮಗಳು ಮತ್ತು ಸೂತ್ರಗಳನ್ನು ಹುಡುಕಿ" else "Search chapters, laws, formulas or topics") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = onResetSearch,
                                    modifier = Modifier.testTag("reset_search_button")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Reset search")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("study_search_input")
                    )

                    UnifiedCurriculumControls(
                        selectedSubject = uiState.selectedSubject,
                        chapters = availableChapters.ifEmpty { SyllabusRepository.chapters },
                        selectedChapter = currentChapter,
                        selectedTopicId = uiState.selectedTopicId,
                        selectedLanguage = uiState.selectedLanguage,
                        onSubjectSelected = onSelectSubject,
                        onChapterSelected = onSelectChapter,
                        onTopicSelected = onSelectTopic,
                        onLanguageSelected = onSelectLanguage
                    )
                }
            }
        }

        if (uiState.recentChapterIds.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Continue Learning",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.recentChapterIds.forEach { chId ->
                            val ch = SyllabusRepository.getChapterById(chId)
                            FilterChip(
                                selected = ch.id == currentChapter.id,
                                onClick = { onSelectChapter(ch.id) },
                                label = { Text(if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಅಧ್ಯಾಯ ${ch.chapterNumber}: ${ch.titleKn}" else "Ch ${ch.chapterNumber}: ${ch.titleEn}") }
                            )
                        }
                    }
                }
            }
        }

        item {
            ChapterWorkspacePanel(
                chapter = currentChapter,
                selectedTopicId = uiState.selectedTopicId,
                language = uiState.selectedLanguage,
                chapterProgress = chapterProgressMap[currentChapter.id],
                onToggleTopicCompleted = onToggleTopicCompleted,
                onSaveNotesOffline = onSaveChapterNotesOffline,
                onAskAiTeacher = onAskAiTeacher,
                onOpenLabForChapter = onOpenLabForChapter,
                onOpenPyqTab = onOpenPyqTab,
                onWatchVideo = { activeVideo = it },
                textbookActionLabel = when {
                    chapterBook?.status == DownloadStatus.DOWNLOADED -> if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಪಠ್ಯಪುಸ್ತಕ ತೆರೆಯಿರಿ" else "Open Textbook PDF"
                    chapterBook?.status == DownloadStatus.DOWNLOADING -> if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಡೌನ್‌ಲೋಡ್ ಆಗುತ್ತಿದೆ…" else "Textbook downloading…"
                    else -> if (uiState.selectedLanguage == StudyLanguage.KANNADA) "ಪಠ್ಯಪುಸ್ತಕ ಡೌನ್‌ಲೋಡ್" else "Download Textbook"
                },
                onOpenTextbook = {
                    chapterBook?.let { book ->
                        if (book.status == DownloadStatus.DOWNLOADED) activeBookForReader = book else onDownloadBook(book.id)
                    }
                }
            )
        }


    }

    if (showVerificationDialog) {
        AlertDialog(
            onDismissRequest = { showVerificationDialog = false },
            title = { Text("Syllabus Reference & Historical Exclusions") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(uiState.verificationInfo.statusMessage, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Active Chapters: ${uiState.verificationInfo.activeChapterCount} | Edition: ${uiState.verificationInfo.editionLabel}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Historical rationalized exclusions — current-year confirmation pending:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    SyllabusRepository.rationalizedDeletions.forEach { del ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Text("Old Ch ${del.oldChapterNumber}: ${del.oldChapterTitle}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Text("${del.status} — ${del.notes}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onRefreshVerification()
                    showVerificationDialog = false
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ping Portals Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showVerificationDialog = false }) { Text("Close") }
            }
        )
    }

    activeBookForReader?.let { book ->
        OfflinePdfReaderDialog(
            book = book,
            onProgressChanged = { page, marks ->
                onUpdateBookReading(book.id, page, marks)
            },
            onDismiss = { activeBookForReader = null }
        )
    }

    activeVideo?.let { video ->
        YouTubePlayerDialog(
            video = video,
            onDismiss = { activeVideo = null }
        )
    }
}

