package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.core.content.ContextCompat
import com.example.data.ai.AiProviderStatus
import com.example.data.curriculum.SyllabusRepository
import com.example.model.AiConversationRecord
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.model.TeachingMode
import com.example.ui.components.UnifiedCurriculumControls
import com.example.viewmodel.AiChatMessage
import com.example.viewmodel.AiTeacherUiState
import com.example.viewmodel.CurriculumUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTeacherScreen(
    curriculumState: CurriculumUiState,
    selectedMode: TeachingMode,
    messages: List<AiChatMessage>,
    uiState: AiTeacherUiState,
    draftQuestion: String,
    providerStatus: AiProviderStatus,
    savedHistory: List<AiConversationRecord>,
    onSelectSubject: (SubjectFilter) -> Unit,
    onSelectChapter: (String) -> Unit,
    onSelectTopic: (String) -> Unit,
    onSelectLanguage: (StudyLanguage) -> Unit,
    onSelectMode: (TeachingMode) -> Unit,
    onDraftChanged: (String) -> Unit,
    onSendQuestion: (question: String, chapterId: String, language: StudyLanguage) -> Unit,
    onClearError: () -> Unit
) {
    val context = LocalContext.current
    var modeDropdownExpanded by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }
    var voiceStatusMessage by remember { mutableStateOf<String?>(null) }
    var showSavedHistory by remember { mutableStateOf(false) }

    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember(context) {
        var instance: TextToSpeech? = null
        instance = TextToSpeech(context) { status ->
            ttsReady = (status == TextToSpeech.SUCCESS)
        }
        instance
    }

    val speechRecognizer = remember(context) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            null
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                speechRecognizer?.destroy()
                tts.stop()
                tts.shutdown()
            } catch (_: Exception) {
            }
        }
    }

    fun startVoiceRecognition() {
        if (speechRecognizer == null) {
            voiceStatusMessage = "Voice recognition service is unavailable on this device."
            return
        }
        voiceStatusMessage = null
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            val langTag = if (curriculumState.selectedLanguage == StudyLanguage.KANNADA) "kn-IN" else "en-IN"
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask your SSLC Science doubt in English, ಕನ್ನಡ or Kanglish")
        }
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { isListening = true }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onError(error: Int) {
                isListening = false
                voiceStatusMessage = "Could not capture clear voice (code $error). You can type or tap mic again."
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val spoken = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                if (!spoken.isNullOrBlank()) {
                    onDraftChanged(spoken)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                if (!partial.isNullOrBlank()) {
                    onDraftChanged(partial)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer.startListening(intent)
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startVoiceRecognition()
        } else {
            voiceStatusMessage = "Microphone permission denied. Type your question or grant mic permission to speak."
        }
    }

    fun speakAnswer(text: String, language: StudyLanguage) {
        if (!ttsReady) return
        val locale = if (language == StudyLanguage.KANNADA) Locale.forLanguageTag("kn-IN") else Locale.forLanguageTag("en-IN")
        tts.language = locale
        tts.speak(text.take(2000), TextToSpeech.QUEUE_FLUSH, null, "ai_teacher_tts")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ai_teacher_screen"),
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
                                text = "Bilingual SSLC Science AI Teacher",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Understands English, ಕನ್ನಡ & Kanglish",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (providerStatus is AiProviderStatus.GeminiCloudReady) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.tertiaryContainer
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (providerStatus is AiProviderStatus.GeminiCloudReady) Icons.Default.AutoAwesome else Icons.Default.CloudOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (providerStatus is AiProviderStatus.GeminiCloudReady) "Gemini AI" else "Offline RAG",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
                        onLanguageSelected = onSelectLanguage
                    )

                    ExposedDropdownMenuBox(
                        expanded = modeDropdownExpanded,
                        onExpandedChange = { modeDropdownExpanded = !modeDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedMode.title,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = { Text("Teaching Mode (14 Modes)") },
                            supportingText = { Text(selectedMode.shortDescription) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modeDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("dropdown_teaching_mode")
                        )
                        ExposedDropdownMenu(
                            expanded = modeDropdownExpanded,
                            onDismissRequest = { modeDropdownExpanded = false }
                        ) {
                            TeachingMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(mode.title, fontWeight = FontWeight.Medium)
                                            Text(mode.shortDescription, style = MaterialTheme.typography.bodySmall)
                                        }
                                    },
                                    onClick = {
                                        modeDropdownExpanded = false
                                        onSelectMode(mode)
                                    },
                                    modifier = Modifier.testTag("mode_option_${mode.name.lowercase()}")
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Ohm's law andre enu?",
                            "Balance FeSO4 thermal decomposition",
                            "Explain human heart double circulation",
                            "Difference between roasting and calcination"
                        ).forEach { prompt ->
                            AssistChip(
                                onClick = { onDraftChanged(prompt) },
                                label = { Text(prompt) }
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = draftQuestion,
                        onValueChange = onDraftChanged,
                        label = { Text("Type or speak your Science doubt") },
                        placeholder = { Text("e.g., Current andre enu? / Explain Lens Formula / ಪ್ಲಾಸ್ಟರ್ ಆಫ್ ಪ್ಯಾರಿಸ್") },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_question_input")
                    )

                    if (!voiceStatusMessage.isNullOrBlank()) {
                        Text(
                            text = voiceStatusMessage.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (isListening) {
                                    speechRecognizer?.stopListening()
                                    isListening = false
                                } else {
                                    val hasMic = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasMic) {
                                        startVoiceRecognition()
                                    } else {
                                        micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("voice_mic_button")
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = if (isListening) "Stop listening" else "Speak question",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isListening) "Listening..." else "Speak")
                        }

                        Button(
                            onClick = {
                                onSendQuestion(
                                    draftQuestion,
                                    curriculumState.selectedChapterId,
                                    curriculumState.selectedLanguage
                                )
                            },
                            enabled = draftQuestion.isNotBlank() && uiState !is AiTeacherUiState.Loading && uiState !is AiTeacherUiState.Streaming,
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("send_ai_question_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask Science AI")
                        }
                    }
                }
            }
        }

        if (uiState is AiTeacherUiState.Loading) {
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Retrieving KTBS textbook context & preparing explanation...")
                    }
                }
            }
        }

        if (uiState is AiTeacherUiState.Streaming) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("AI Teacher answering...", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text(uiState.partialText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        if (uiState is AiTeacherUiState.Error) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(uiState.message, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        OutlinedButton(onClick = onClearError) { Text("Dismiss") }
                    }
                }
            }
        }

        items(messages.reversed(), key = { it.id }) { msg ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (msg.isStudent) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
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
                        Text(
                            text = if (msg.isStudent) "Student • ${msg.teachingMode.title}" else "AI Teacher • ${msg.providerLabel}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (!msg.isStudent) {
                            IconButton(
                                onClick = { speakAnswer(msg.text, msg.language) },
                                modifier = Modifier.testTag("tts_speak_${msg.id}")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen to explanation")
                            }
                        }
                    }

                    if (!msg.isStudent && msg.questionType.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, label = { Text(msg.questionType) })
                        }
                    }

                    Text(msg.text, style = MaterialTheme.typography.bodyMedium)

                    if (!msg.isStudent && msg.sourceCitation.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Grounding: ${msg.sourceCitation} (AI Teacher explanation, not official KSEAB key)",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        if (savedHistory.isNotEmpty()) {
            item {
                FilterChip(
                    selected = showSavedHistory,
                    onClick = { showSavedHistory = !showSavedHistory },
                    leadingIcon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    label = { Text(if (showSavedHistory) "Hide Cloud History (${savedHistory.size})" else "Show Saved Cloud Q&A (${savedHistory.size})") }
                )
            }
            if (showSavedHistory) {
                items(savedHistory.take(10), key = { it.id }) { item ->
                    Card(shape = RoundedCornerShape(14.dp)) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("${item.teachingMode} • ${item.sourceCitation}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(item.answer.take(240) + if (item.answer.length > 240) "..." else "", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
