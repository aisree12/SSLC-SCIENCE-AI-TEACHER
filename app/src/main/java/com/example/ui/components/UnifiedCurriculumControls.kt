package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ScienceDivision
import com.example.model.ScienceTopic
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.model.SyllabusChapter
import com.example.ui.theme.BiologyAccent
import com.example.ui.theme.BiologyContainer
import com.example.ui.theme.ChemistryAccent
import com.example.ui.theme.ChemistryContainer
import com.example.ui.theme.PhysicsAccent
import com.example.ui.theme.PhysicsContainer

fun divisionAccentColor(division: ScienceDivision): Color = when (division) {
    ScienceDivision.PHYSICS -> PhysicsAccent
    ScienceDivision.CHEMISTRY -> ChemistryAccent
    ScienceDivision.BIOLOGY -> BiologyAccent
}

fun divisionContainerColor(division: ScienceDivision): Color = when (division) {
    ScienceDivision.PHYSICS -> PhysicsContainer
    ScienceDivision.CHEMISTRY -> ChemistryContainer
    ScienceDivision.BIOLOGY -> BiologyContainer
}

fun divisionIcon(division: ScienceDivision): ImageVector = when (division) {
    ScienceDivision.PHYSICS -> Icons.Default.Bolt
    ScienceDivision.CHEMISTRY -> Icons.Default.Science
    ScienceDivision.BIOLOGY -> Icons.Default.Biotech
}

@Composable
fun DivisionBadge(division: ScienceDivision, showKannada: Boolean = false, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = divisionContainerColor(division)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = divisionIcon(division),
                contentDescription = division.displayName,
                tint = divisionAccentColor(division),
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = if (showKannada) "${division.displayName} • ${division.kannadaName}" else division.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = divisionAccentColor(division),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedCurriculumControls(
    selectedSubject: SubjectFilter,
    chapters: List<SyllabusChapter>,
    selectedChapter: SyllabusChapter,
    selectedTopicId: String,
    selectedLanguage: StudyLanguage,
    onSubjectSelected: (SubjectFilter) -> Unit,
    onChapterSelected: (String) -> Unit,
    onTopicSelected: (String) -> Unit,
    onLanguageSelected: (StudyLanguage) -> Unit,
    modifier: Modifier = Modifier,
    showTopicSelector: Boolean = true
) {
    var subjectExpanded by remember { mutableStateOf(false) }
    var chapterExpanded by remember { mutableStateOf(false) }
    var topicExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

    val topics: List<ScienceTopic> = selectedChapter.topics
    val currentTopic = topics.firstOrNull { it.id == selectedTopicId } ?: topics.firstOrNull()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = subjectExpanded,
                onExpandedChange = { subjectExpanded = !subjectExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = if (selectedLanguage == StudyLanguage.KANNADA) when (selectedSubject) { SubjectFilter.ALL -> "ಎಲ್ಲಾ ವಿಜ್ಞಾನ"; SubjectFilter.PHYSICS -> "ಭೌತಶಾಸ್ತ್ರ"; SubjectFilter.CHEMISTRY -> "ರಸಾಯನಶಾಸ್ತ್ರ"; SubjectFilter.BIOLOGY -> "ಜೀವಶಾಸ್ತ್ರ" } else selectedSubject.label,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text(if (selectedLanguage == StudyLanguage.KANNADA) "ವಿಷಯ" else "Subject") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("dropdown_subject")
                )
                ExposedDropdownMenu(
                    expanded = subjectExpanded,
                    onDismissRequest = { subjectExpanded = false }
                ) {
                    SubjectFilter.entries.forEach { filter ->
                        DropdownMenuItem(
                            text = { Text(if (selectedLanguage == StudyLanguage.KANNADA) when (filter) { SubjectFilter.ALL -> "ಎಲ್ಲಾ ವಿಜ್ಞಾನ"; SubjectFilter.PHYSICS -> "ಭೌತಶಾಸ್ತ್ರ"; SubjectFilter.CHEMISTRY -> "ರಸಾಯನಶಾಸ್ತ್ರ"; SubjectFilter.BIOLOGY -> "ಜೀವಶಾಸ್ತ್ರ" } else filter.label) },
                            onClick = {
                                subjectExpanded = false
                                onSubjectSelected(filter)
                            },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("subject_option_${filter.name.lowercase()}")
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = !languageExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = when (selectedLanguage) {
                        StudyLanguage.ENGLISH -> "English"
                        StudyLanguage.KANNADA -> "ಕನ್ನಡ"
                        StudyLanguage.BILINGUAL -> "English"
                    },
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text(if (selectedLanguage == StudyLanguage.KANNADA) "ಮಾಧ್ಯಮ" else "Medium") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("dropdown_language")
                )
                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    StudyLanguage.entries.filter { it != StudyLanguage.BILINGUAL }.forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang.label) },
                            onClick = {
                                languageExpanded = false
                                onLanguageSelected(lang)
                            },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("language_option_${lang.name.lowercase()}")
                        )
                    }
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = chapterExpanded,
            onExpandedChange = { chapterExpanded = !chapterExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            val chapterDisplay = if (selectedLanguage == StudyLanguage.KANNADA) {
                "Ch ${selectedChapter.chapterNumber}: ${selectedChapter.titleKn}"
            } else {
                "Ch ${selectedChapter.chapterNumber}: ${selectedChapter.titleEn}"
            }
            OutlinedTextField(
                value = chapterDisplay,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text(if (selectedLanguage == StudyLanguage.KANNADA) "ಅಧ್ಯಾಯಗಳು (${chapters.size})" else "Chapter (${chapters.size} available)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = chapterExpanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("dropdown_chapter")
            )
            ExposedDropdownMenu(
                expanded = chapterExpanded,
                onDismissRequest = { chapterExpanded = false }
            ) {
                chapters.forEach { ch ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (selectedLanguage == StudyLanguage.KANNADA) "ಅಧ್ಯಾಯ ${ch.chapterNumber}: ${ch.titleKn}" else "Ch ${ch.chapterNumber}: ${ch.titleEn}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        onClick = {
                            chapterExpanded = false
                            onChapterSelected(ch.id)
                        },
                        modifier = Modifier
                            .heightIn(min = 52.dp)
                            .testTag("chapter_option_${ch.id}")
                    )
                }
            }
        }

        if (showTopicSelector && topics.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = topicExpanded,
                onExpandedChange = { topicExpanded = !topicExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                val topicLabel = currentTopic?.let {
                    if (selectedLanguage == StudyLanguage.KANNADA) it.kannadaTitle else it.title
                } ?: if (selectedLanguage == StudyLanguage.KANNADA) "ವಿಷಯಾಂಶ ಆಯ್ಕೆಮಾಡಿ" else "Select Topic"
                OutlinedTextField(
                    value = topicLabel,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text(if (selectedLanguage == StudyLanguage.KANNADA) "ವಿಷಯಾಂಶ" else "Topic") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = topicExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("dropdown_topic")
                )
                ExposedDropdownMenu(
                    expanded = topicExpanded,
                    onDismissRequest = { topicExpanded = false }
                ) {
                    topics.forEach { topic ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(topic.title, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        topic.kannadaTitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                topicExpanded = false
                                onTopicSelected(topic.id)
                            },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("topic_option_${topic.id}")
                        )
                    }
                }
            }
        }
    }
}
