package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.data.lab.SimulationAndDiagramCatalog
import com.example.model.ScienceDiagram
import com.example.model.ScienceSimulation
import com.example.model.StudyLanguage
import com.example.ui.components.DivisionBadge
import kotlin.math.roundToInt

@Composable
fun ScienceLabAndDiagramScreen(
    initialChapterId: String,
    language: StudyLanguage,
    onAskAiTeacherAboutLab: (chapterId: String, prompt: String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var activeStudioMode by remember { mutableIntStateOf(0) } // 0 = Interactive Lab, 1 = Diagram Studio
    var isKannadaLabels by remember(language) { mutableStateOf(language == StudyLanguage.KANNADA) }

    val allSims = SimulationAndDiagramCatalog.simulations
    val defaultSim = remember(initialChapterId) {
        allSims.firstOrNull { it.chapterId == initialChapterId } ?: allSims.first()
    }
    var selectedSimId by remember(initialChapterId) { mutableStateOf(defaultSim.id) }
    val activeSim: ScienceSimulation = allSims.firstOrNull { it.id == selectedSimId } ?: allSims.first()

    val allDiagrams = SimulationAndDiagramCatalog.diagrams
    val defaultDiagram = remember(initialChapterId) {
        allDiagrams.firstOrNull { it.chapterId == initialChapterId } ?: allDiagrams.first()
    }
    var selectedDiagramId by remember(initialChapterId) { mutableStateOf(defaultDiagram.id) }
    val activeDiagram: ScienceDiagram = allDiagrams.firstOrNull { it.id == selectedDiagramId } ?: allDiagrams.first()

    var isRunning by remember { mutableStateOf(true) }
    var stepIndex by remember { mutableIntStateOf(0) }
    var showLabels by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("science_lab_diagram_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("lab_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Study Centre")
                    }
                    Column {
                        Text(
                            text = "Science Lab & Diagram Studio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Interactive Simulations & KSEAB Diagram Practice",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = activeStudioMode == 0,
                        onClick = { activeStudioMode = 0 },
                        label = { Text("Interactive Lab") },
                        modifier = Modifier.testTag("lab_mode_simulations")
                    )
                    FilterChip(
                        selected = activeStudioMode == 1,
                        onClick = { activeStudioMode = 1 },
                        label = { Text("Diagram Studio") },
                        modifier = Modifier.testTag("lab_mode_diagrams")
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("KN", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(checked = isKannadaLabels, onCheckedChange = { isKannadaLabels = it })
                }
            }
        }

        if (activeStudioMode == 0) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allSims.forEach { sim ->
                        FilterChip(
                            selected = sim.id == activeSim.id,
                            onClick = {
                                selectedSimId = sim.id
                                stepIndex = 0
                            },
                            label = { Text(if (isKannadaLabels) sim.titleKn else sim.titleEn) }
                        )
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AssistChip(onClick = {}, label = { Text(activeSim.category.label) })
                        Text(
                            text = if (isKannadaLabels) activeSim.titleKn else activeSim.titleEn,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isKannadaLabels) activeSim.summaryKn else activeSim.summaryEn,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = activeSim.equationOrLaw,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth().padding(10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(onClick = { isRunning = !isRunning }) {
                                Icon(if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isRunning) "Pause" else "Start")
                            }
                            OutlinedButton(onClick = { stepIndex++ }) {
                                Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Step")
                            }
                            OutlinedButton(onClick = {
                                stepIndex = 0
                                isRunning = true
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text("Labels", style = MaterialTheme.typography.labelSmall)
                            Switch(checked = showLabels, onCheckedChange = { showLabels = it })
                        }

                        InteractiveSimulationCanvas(
                            simulationId = activeSim.id,
                            isRunning = isRunning,
                            stepIndex = stepIndex,
                            showLabels = showLabels,
                            isKannada = isKannadaLabels
                        )

                        Button(
                            onClick = {
                                onAskAiTeacherAboutLab(
                                    activeSim.chapterId,
                                    "Explain the '${activeSim.titleEn}' experiment, law (${activeSim.equationOrLaw}), and board exam questions on it."
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("ask_ai_about_experiment")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ask AI Teacher About This Experiment")
                        }
                    }
                }
            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allDiagrams.forEach { d ->
                        FilterChip(
                            selected = d.id == activeDiagram.id,
                            onClick = { selectedDiagramId = d.id },
                            label = { Text(if (isKannadaLabels) d.titleKn else d.titleEn) }
                        )
                    }
                }
            }

            item {
                DiagramStudioCard(
                    diagram = activeDiagram,
                    isKannada = isKannadaLabels,
                    onAskAiTeacher = {
                        onAskAiTeacherAboutLab(
                            activeDiagram.chapterId,
                            "Teach me how to draw and label '${activeDiagram.titleEn}' for KSEAB SSLC board exams."
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DiagramStudioCard(
    diagram: ScienceDiagram,
    isKannada: Boolean,
    onAskAiTeacher: () -> Unit
) {
    var practiceQuizMode by remember(diagram.id) { mutableStateOf(false) }
    var selectedHotspotId by remember(diagram.id) { mutableStateOf(diagram.hotspots.first().id) }
    val guessedLabels = remember(diagram.id) { mutableStateMapOf<String, String>() }

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
                DivisionBadge(division = diagram.division)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = !practiceQuizMode, onClick = { practiceQuizMode = false }, label = { Text("Labelled") })
                    FilterChip(selected = practiceQuizMode, onClick = { practiceQuizMode = true }, label = { Text("Label Quiz") })
                }
            }

            Text(
                text = if (isKannada) diagram.titleKn else diagram.titleEn,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Clean schematic frame representing the scientific organ/apparatus
                    drawRoundRect(
                        color = Color(0xFFF1F5F9),
                        topLeft = Offset.Zero,
                        size = size
                    )
                    drawOval(
                        color = Color(0xFF94A3B8),
                        topLeft = Offset(size.width * 0.16f, size.height * 0.14f),
                        size = Size(size.width * 0.68f, size.height * 0.72f),
                        style = Stroke(width = 4f)
                    )
                    diagram.hotspots.forEach { spot ->
                        val sx = spot.normalizedX * size.width
                        val sy = spot.normalizedY * size.height
                        val isSelected = spot.id == selectedHotspotId
                        drawCircle(
                            color = if (isSelected) Color(0xFFD97706) else Color(0xFF0F766E),
                            radius = if (isSelected) 12f else 9f,
                            center = Offset(sx, sy)
                        )
                    }
                }

                diagram.hotspots.forEachIndexed { idx, spot ->
                    val labelText = when {
                        !practiceQuizMode -> if (isKannada) spot.labelKn else spot.labelEn
                        guessedLabels[spot.id] != null -> guessedLabels[spot.id]!!
                        else -> "Tap Pin #${idx + 1}"
                    }
                    Surface(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (spot.normalizedX * 680f).roundToInt().coerceIn(8, 520),
                                    y = (spot.normalizedY * 460f).roundToInt().coerceIn(8, 420)
                                )
                            },
                        onClick = { selectedHotspotId = spot.id },
                        shape = RoundedCornerShape(8.dp),
                        color = if (spot.id == selectedHotspotId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = labelText,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (spot.id == selectedHotspotId) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            val activeSpot = diagram.hotspots.firstOrNull { it.id == selectedHotspotId }
            if (activeSpot != null) {
                if (!practiceQuizMode) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text("${activeSpot.labelEn} (${activeSpot.labelKn})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(activeSpot.functionDesc, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    Text("Select correct label for selected pin:", style = MaterialTheme.typography.labelLarge)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        diagram.hotspots.forEach { candidate ->
                            val candidateLabel = if (isKannada) candidate.labelKn else candidate.labelEn
                            FilterChip(
                                selected = guessedLabels[activeSpot.id] == candidateLabel,
                                onClick = { guessedLabels[activeSpot.id] = candidateLabel },
                                label = { Text(candidateLabel) }
                            )
                        }
                    }
                    val correctCount = diagram.hotspots.count { spot ->
                        val expected = if (isKannada) spot.labelKn else spot.labelEn
                        guessedLabels[spot.id] == expected
                    }
                    Text(
                        text = "Label Quiz Score: $correctCount / ${diagram.hotspots.size}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text("Step-by-Step Exam Drawing Guide", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            diagram.drawingSteps.forEach { step ->
                Text(step, style = MaterialTheme.typography.bodySmall)
            }
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.tertiaryContainer) {
                Text("Board Exam Tip: ${diagram.examTip}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(10.dp))
            }

            Button(
                onClick = onAskAiTeacher,
                modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ask AI Teacher Diagram Tips")
            }
        }
    }
}
