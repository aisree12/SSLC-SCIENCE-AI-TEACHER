package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun InteractiveSimulationCanvas(
    simulationId: String,
    isRunning: Boolean,
    stepIndex: Int,
    showLabels: Boolean,
    isKannada: Boolean
) {
    var animPhase by remember(simulationId) { mutableFloatStateOf(0f) }

    LaunchedEffect(isRunning, simulationId) {
        while (isRunning) {
            animPhase = (animPhase + 0.08f) % (2f * PI.toFloat())
            delay(40L)
        }
    }

    when (simulationId) {
        "sim_phy_optics" -> OpticsRayLab(showLabels, isKannada)
        "sim_phy_ohms_circuits" -> OhmsLawCircuitLab(animPhase, showLabels, isKannada)
        "sim_phy_electromagnet" -> SolenoidLab(animPhase, showLabels, isKannada)
        "sim_phy_eye" -> HumanEyeLab(showLabels, isKannada)
        "sim_chem_ph" -> PhScaleLab(showLabels, isKannada)
        "sim_chem_balancer" -> EquationBalancerLab(stepIndex, isKannada)
        "sim_chem_reactivity" -> ReactivityDisplacementLab(stepIndex, isKannada)
        "sim_chem_carbon" -> CarbonFunctionalLab(isKannada)
        "sim_bio_heart" -> HeartCirculationLab(animPhase, stepIndex, showLabels, isKannada)
        "sim_bio_nephron" -> NephronSimulationLab(animPhase, stepIndex, showLabels)
        "sim_bio_reflex" -> ReflexArcLab(animPhase, stepIndex, showLabels)
        "sim_bio_ecosystem" -> EnergyPyramidLab(showLabels, isKannada)
        else -> OpticsRayLab(showLabels, isKannada)
    }
}

@Composable
private fun OpticsRayLab(showLabels: Boolean, isKannada: Boolean) {
    var focalLength by remember { mutableFloatStateOf(15f) }
    var objectDistance by remember { mutableFloatStateOf(30f) }
    var isConvexLens by remember { mutableStateOf(true) }

    val u = -objectDistance
    val f = focalLength
    val denom = (1f / f) + (1f / u)
    val v = if (abs(denom) < 0.0005f) 999f else 1f / denom
    val mag = v / u

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = isConvexLens, onClick = { isConvexLens = true }, label = { Text("Convex Lens") })
            FilterChip(selected = !isConvexLens, onClick = { isConvexLens = false }, label = { Text("Concave Mirror") })
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            val midY = size.height / 2f
            val centerX = size.width / 2f
            val scale = size.width / 120f

            // Principal axis
            drawLine(Color(0xFF64748B), Offset(12f, midY), Offset(size.width - 12f, midY), strokeWidth = 2f)

            // Lens or Mirror at centerX
            if (isConvexLens) {
                drawOval(
                    color = Color(0x5538BDF8),
                    topLeft = Offset(centerX - 10f, 22f),
                    size = Size(20f, size.height - 44f)
                )
            } else {
                drawArc(
                    color = Color(0xFF1D4ED8),
                    startAngle = -75f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(centerX - 30f, 20f),
                    size = Size(40f, size.height - 40f),
                    style = Stroke(width = 5f)
                )
            }

            // Focal points
            val fPx = f * scale
            drawCircle(Color(0xFFD97706), radius = 5f, center = Offset(centerX - fPx, midY))
            drawCircle(Color(0xFFD97706), radius = 5f, center = Offset(centerX + fPx, midY))

            // Object arrow
            val objX = (centerX + u * scale).coerceIn(20f, centerX - 12f)
            val objH = 44f
            drawLine(Color(0xFF15803D), Offset(objX, midY), Offset(objX, midY - objH), strokeWidth = 5f)

            // Image arrow
            val imgX = if (isConvexLens) (centerX + v * scale) else (centerX - v * scale)
            val clampedImgX = imgX.coerceIn(20f, size.width - 20f)
            val imgH = (objH * mag).coerceIn(-65f, 65f)
            drawLine(Color(0xFFDC2626), Offset(clampedImgX, midY), Offset(clampedImgX, midY - imgH), strokeWidth = 5f)

            // Ray 1: parallel -> focus -> image
            drawLine(Color(0xFF2563EB), Offset(objX, midY - objH), Offset(centerX, midY - objH), strokeWidth = 2.5f)
            drawLine(Color(0xFF2563EB), Offset(centerX, midY - objH), Offset(clampedImgX, midY - imgH), strokeWidth = 2.5f)
            // Ray 2: through optical center / pole
            drawLine(Color(0xFF0F766E), Offset(objX, midY - objH), Offset(clampedImgX, midY - imgH), strokeWidth = 2.5f)
        }
        if (showLabels) {
            val vText = if (abs(v) > 500f) "Infinity" else "%.1f cm".format(v)
            Text(
                text = if (isKannada) {
                    "u = ${objectDistance.toInt()} cm | f = ${f.toInt()} cm | v = $vText | ವರ್ಧನೆ m = ${"%.2f".format(mag)}"
                } else {
                    "u = -${objectDistance.toInt()} cm | f = +${f.toInt()} cm | v = $vText | Magnification m = ${"%.2f".format(mag)}"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text("Object Distance u (${objectDistance.toInt()} cm)", style = MaterialTheme.typography.bodySmall)
        Slider(value = objectDistance, onValueChange = { objectDistance = it }, valueRange = 8f..50f)
        Text("Focal Length f (${focalLength.toInt()} cm)", style = MaterialTheme.typography.bodySmall)
        Slider(value = focalLength, onValueChange = { focalLength = it }, valueRange = 10f..25f)
    }
}

@Composable
private fun OhmsLawCircuitLab(animPhase: Float, showLabels: Boolean, isKannada: Boolean) {
    var voltage by remember { mutableFloatStateOf(12f) }
    var r1 by remember { mutableFloatStateOf(4f) }
    var r2 by remember { mutableFloatStateOf(6f) }
    var isSeries by remember { mutableStateOf(true) }

    val rEq = if (isSeries) (r1 + r2) else ((r1 * r2) / (r1 + r2))
    val current = voltage / rEq
    val power = voltage * current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = isSeries, onClick = { isSeries = true }, label = { Text("Series Circuit") })
            FilterChip(selected = !isSeries, onClick = { isSeries = false }, label = { Text("Parallel Circuit") })
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            val left = 50f
            val right = size.width - 50f
            val top = 36f
            val bottom = size.height - 36f
            val wireColor = Color(0xFF1E3A8A)

            drawRect(wireColor, Offset(left, top), Size(right - left, bottom - top), style = Stroke(width = 4f))

            if (!isSeries) {
                val midY = (top + bottom) / 2f
                drawLine(wireColor, Offset(left + 40f, midY), Offset(right - 40f, midY), strokeWidth = 4f)
                drawRect(Color(0xFF0F766E), Offset((left + right) / 2f - 36f, top - 12f), Size(72f, 24f))
                drawRect(Color(0xFFD97706), Offset((left + right) / 2f - 36f, midY - 12f), Size(72f, 24f))
            } else {
                drawRect(Color(0xFF0F766E), Offset(left + (right - left) * 0.25f, top - 12f), Size(64f, 24f))
                drawRect(Color(0xFFD97706), Offset(left + (right - left) * 0.60f, top - 12f), Size(64f, 24f))
            }

            // Battery bar at bottom
            drawRect(Color(0xFFDC2626), Offset((left + right) / 2f - 24f, bottom - 12f), Size(48f, 24f))

            // Moving charge dots along circuit
            val progress = (animPhase / (2f * PI.toFloat()))
            val dotX = left + (right - left) * progress
            drawCircle(Color(0xFF26A69A), radius = 7f, center = Offset(dotX, top))
            drawCircle(Color(0xFF26A69A), radius = 7f, center = Offset(right - (right - left) * progress, bottom))
        }
        if (showLabels) {
            Text(
                text = if (isKannada) {
                    "ಸಮತೂಕ ರೋಧ R_eq = ${"%.2f".format(rEq)} Ω | ವಿದ್ಯುತ್ ಪ್ರವಾಹ I = ${"%.2f".format(current)} A | ಸಾಮರ್ಥ್ಯ P = ${"%.1f".format(power)} W"
                } else {
                    "R_eq = ${"%.2f".format(rEq)} Ω | Current I = ${"%.2f".format(current)} A | Power P = ${"%.1f".format(power)} W"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text("Supply Voltage V = ${voltage.toInt()} V", style = MaterialTheme.typography.bodySmall)
        Slider(value = voltage, onValueChange = { voltage = it }, valueRange = 2f..24f)
        Text("Resistors R1=${r1.toInt()}Ω, R2=${r2.toInt()}Ω", style = MaterialTheme.typography.bodySmall)
        Slider(value = r1, onValueChange = { r1 = it }, valueRange = 1f..20f)
        Slider(value = r2, onValueChange = { r2 = it }, valueRange = 1f..20f)
    }
}

@Composable
private fun SolenoidLab(animPhase: Float, showLabels: Boolean, isKannada: Boolean) {
    var turns by remember { mutableIntStateOf(6) }
    var reverseCurrent by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(if (isKannada) "ವಿದ್ಯುತ್ ದಿಕ್ಕನ್ನು ತಿರುಗಿಸಿ (Reverse Current)" else "Reverse Current Polarity")
            Switch(checked = reverseCurrent, onCheckedChange = { reverseCurrent = it })
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(165.dp)) {
            val cy = size.height / 2f
            val startX = size.width * 0.22f
            val endX = size.width * 0.78f
            val spacing = (endX - startX) / turns

            // Iron core bar
            drawRect(Color(0xFF94A3B8), Offset(startX - 10f, cy - 22f), Size(endX - startX + 20f, 44f))

            // Solenoid coils
            for (i in 0 until turns) {
                val x = startX + i * spacing
                drawArc(
                    color = Color(0xFFD97706),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(x, cy - 30f),
                    size = Size(spacing * 0.7f, 60f),
                    style = Stroke(width = 4f)
                )
            }

            // Field lines animated
            val pulse = 4f * sin(animPhase)
            drawOval(
                color = Color(0x881D4ED8),
                topLeft = Offset(startX - 40f - pulse, cy - 58f),
                size = Size(endX - startX + 80f + pulse * 2, 116f),
                style = Stroke(width = 2.5f)
            )
            // Pole markers
            val leftColor = if (reverseCurrent) Color(0xFF2563EB) else Color(0xFFDC2626)
            val rightColor = if (reverseCurrent) Color(0xFFDC2626) else Color(0xFF2563EB)
            drawCircle(leftColor, 14f, Offset(startX - 24f, cy))
            drawCircle(rightColor, 14f, Offset(endX + 24f, cy))
        }
        if (showLabels) {
            val leftPole = if (reverseCurrent) "South (S)" else "North (N)"
            val rightPole = if (reverseCurrent) "North (N)" else "South (S)"
            Text("Left Pole: $leftPole • Right Pole: $rightPole • Turns: $turns", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = turns.toFloat(), onValueChange = { turns = it.roundToInt() }, valueRange = 3f..12f)
    }
}

@Composable
private fun HumanEyeLab(showLabels: Boolean, isKannada: Boolean) {
    var mode by remember { mutableIntStateOf(0) } // 0 Normal, 1 Myopia, 2 Hypermetropia
    var applyCorrection by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = mode == 0, onClick = { mode = 0 }, label = { Text("Normal") })
            FilterChip(selected = mode == 1, onClick = { mode = 1 }, label = { Text("Myopia") })
            FilterChip(selected = mode == 2, onClick = { mode = 2 }, label = { Text("Hypermetropia") })
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Apply Corrective Spectacle Lens")
            Switch(checked = applyCorrection, onCheckedChange = { applyCorrection = it })
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(165.dp)) {
            val cy = size.height / 2f
            val retinaX = size.width * 0.80f
            val eyeLensX = size.width * 0.44f
            // Eyeball
            drawCircle(Color(0xFFE2E8F0), radius = 64f, center = Offset(size.width * 0.62f, cy), style = Stroke(width = 5f))
            // Eye lens
            drawOval(Color(0x8838BDF8), Offset(eyeLensX - 10f, cy - 32f), Size(20f, 64f))

            val focalX = when {
                mode == 0 || applyCorrection -> retinaX
                mode == 1 -> retinaX - 42f
                else -> retinaX + 34f
            }
            if (applyCorrection && mode != 0) {
                drawRect(Color(0xFF0F766E), Offset(eyeLensX - 42f, cy - 30f), Size(10f, 60f))
            }
            drawLine(Color(0xFFD97706), Offset(20f, cy - 22f), Offset(eyeLensX, cy - 22f), strokeWidth = 3f)
            drawLine(Color(0xFFD97706), Offset(20f, cy + 22f), Offset(eyeLensX, cy + 22f), strokeWidth = 3f)
            drawLine(Color(0xFFDC2626), Offset(eyeLensX, cy - 22f), Offset(focalX, cy), strokeWidth = 3f)
            drawLine(Color(0xFFDC2626), Offset(eyeLensX, cy + 22f), Offset(focalX, cy), strokeWidth = 3f)
            drawCircle(Color(0xFFDC2626), 6f, Offset(focalX, cy))
        }
        if (showLabels) {
            val info = when {
                mode == 0 -> if (isKannada) "ಸಾಮಾನ್ಯ ಕಣ್ಣು: ಪ್ರತಿಬಿಂಬವು ರೆಟಿನಾದ ಮೇಲೆ ಬೀಳುತ್ತದೆ." else "Normal Eye: Rays focus sharply on the retina."
                mode == 1 && !applyCorrection -> "Myopia (Near-sightedness): Image forms IN FRONT of retina."
                mode == 1 && applyCorrection -> "Myopia Corrected: Concave lens diverges rays back onto retina."
                mode == 2 && !applyCorrection -> "Hypermetropia (Far-sightedness): Image forms BEHIND retina."
                else -> "Hypermetropia Corrected: Convex lens converges rays onto retina."
            }
            Text(info, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun PhScaleLab(showLabels: Boolean, isKannada: Boolean) {
    var ph by remember { mutableFloatStateOf(7f) }
    val phRounded = ph.roundToInt().coerceIn(0, 14)
    val color = when {
        phRounded <= 2 -> Color(0xFFDC2626)
        phRounded <= 5 -> Color(0xFFF97316)
        phRounded == 6 -> Color(0xFFEAB308)
        phRounded == 7 -> Color(0xFF16A34A)
        phRounded <= 10 -> Color(0xFF0D9488)
        else -> Color(0xFF4F46E5)
    }
    val sample = when (phRounded) {
        0, 1 -> "Concentrated HCl / Gastric Juice"
        2, 3 -> "Lemon Juice / Vinegar"
        4, 5 -> "Tomato Juice / Acid Rain"
        6 -> "Milk"
        7 -> "Pure Water (Neutral)"
        8, 9 -> "Baking Soda Solution"
        10, 11 -> "Milk of Magnesia"
        else -> "Sodium Hydroxide (NaOH)"
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(90.dp)) {
            val barW = size.width / 15f
            for (i in 0..14) {
                val c = Color.hsv(hue = (i * 18f).coerceAtMost(260f), saturation = 0.85f, value = 0.85f)
                drawRect(c, Offset(i * barW, 18f), Size(barW - 2f, 44f))
            }
            drawCircle(Color.Black, 8f, Offset(phRounded * barW + barW / 2f, 72f))
        }
        Surface(shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.18f)) {
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Text("pH = $phRounded • $sample", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                if (showLabels) {
                    val litmus = when {
                        phRounded < 7 -> if (isKannada) "ನೀಲಿ ಲಿಟ್ಮಸ್ ಕೆಂಪು ಬಣ್ಣಕ್ಕೆ ತಿರುಗುತ್ತದೆ (ಆಮ್ಲ)" else "Blue Litmus -> Red (Acidic)"
                        phRounded == 7 -> if (isKannada) "ತಟಸ್ಥ ದ್ರಾವಣ" else "Neutral (No colour change in Litmus)"
                        else -> if (isKannada) "ಕೆಂಪು ಲಿಟ್ಮಸ್ ನೀಲಿ ಬಣ್ಣಕ್ಕೆ ತಿರುಗುತ್ತದೆ (ಪ್ರತ್ಯಾಮ್ಲ)" else "Red Litmus -> Blue (Basic)"
                    }
                    Text(litmus, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Slider(value = ph, onValueChange = { ph = it }, valueRange = 0f..14f, steps = 13)
    }
}

@Composable
private fun EquationBalancerLab(stepIndex: Int, isKannada: Boolean) {
    var coeffFeText by remember { mutableStateOf("2") }
    var coeffFe2O3Text by remember { mutableStateOf("1") }
    var coeffSO2Text by remember { mutableStateOf("1") }
    var coeffSO3Text by remember { mutableStateOf("1") }

    val coeffFe = coeffFeText.toIntOrNull()?.coerceIn(1, 20) ?: 1
    val coeffFe2O3 = coeffFe2O3Text.toIntOrNull()?.coerceIn(1, 20) ?: 1
    val coeffSO2 = coeffSO2Text.toIntOrNull()?.coerceIn(1, 20) ?: 1
    val coeffSO3 = coeffSO3Text.toIntOrNull()?.coerceIn(1, 20) ?: 1
    val lhsFe = coeffFe
    val rhsFe = coeffFe2O3 * 2
    val lhsS = coeffFe
    val rhsS = coeffSO2 + coeffSO3
    val lhsO = coeffFe * 4
    val rhsO = coeffFe2O3 * 3 + coeffSO2 * 2 + coeffSO3 * 3
    val isBalanced = lhsFe == rhsFe && lhsS == rhsS && lhsO == rhsO

    @Composable
    fun CoefficientControl(label: String, value: String, onValueChange: (String) -> Unit) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                androidx.compose.material3.IconButton(onClick = {
                    onValueChange(((value.toIntOrNull() ?: 1) - 1).coerceIn(1, 20).toString())
                }) { Text("−", fontWeight = FontWeight.Bold) }
                OutlinedTextField(
                    value = value,
                    onValueChange = { raw -> onValueChange(raw.filter(Char::isDigit).take(2)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(58.dp),
                    textStyle = MaterialTheme.typography.titleSmall
                )
                androidx.compose.material3.IconButton(onClick = {
                    onValueChange(((value.toIntOrNull() ?: 1) + 1).coerceIn(1, 20).toString())
                }) { Text("+", fontWeight = FontWeight.Bold) }
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "${coeffFe}FeSO4(s) → ${coeffFe2O3}Fe2O3(s) + ${coeffSO2}SO2(g) + ${coeffSO3}SO3(g)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isBalanced) Color(0xFF16A34A) else MaterialTheme.colorScheme.primary
        )
        Text(
            text = "LHS: Fe=$lhsFe, S=$lhsS, O=$lhsO   |   RHS: Fe=$rhsFe, S=$rhsS, O=$rhsO",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = if (isKannada) {
                if (isBalanced) "ಸಮತೋಲನವಾಗಿದೆ! ದ್ರವ್ಯರಾಶಿ ಸಂರಕ್ಷಣೆಯ ನಿಯಮ ಸರಿಯಾಗಿದೆ." else "ಪ್ರತಿ ಮೂಲಧಾತುವಿನ ಎಡ ಮತ್ತು ಬಲ ಪರಮಾಣುಗಳ ಸಂಖ್ಯೆಯನ್ನು ಸಮಗೊಳಿಸಿ."
            } else {
                if (isBalanced) "BALANCED! Law of Conservation of Mass verified." else "Adjust coefficients until every atom count matches."
            },
            style = MaterialTheme.typography.labelLarge,
            color = if (isBalanced) Color(0xFF16A34A) else Color(0xFFD97706)
        )
        Text(
            if (isKannada) "ಸಂಖ್ಯೆಯನ್ನು ಟೈಪ್ ಮಾಡಿ ಅಥವಾ + / − ಒತ್ತಿ." else "Type a coefficient or tap + / − to change it.",
            style = MaterialTheme.typography.bodySmall
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            CoefficientControl("FeSO₄", coeffFeText) { coeffFeText = it }
            CoefficientControl("Fe₂O₃", coeffFe2O3Text) { coeffFe2O3Text = it }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            CoefficientControl("SO₂", coeffSO2Text) { coeffSO2Text = it }
            CoefficientControl("SO₃", coeffSO3Text) { coeffSO3Text = it }
        }
        Text(
            if (isKannada) "ಹಂತ ${stepIndex + 1}" else "Practice step ${stepIndex + 1}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ReactivityDisplacementLab(stepIndex: Int, isKannada: Boolean) {
    var metalIdx by remember { mutableIntStateOf(0) } // 0 Zn, 1 Fe, 2 Cu
    val metals = listOf("Zinc (Zn)", "Iron (Fe)", "Copper (Cu)")
    val reactsWithCuSO4 = metalIdx <= 1
    val explanation = when (metalIdx) {
        0 -> "Zn + CuSO4 (blue) -> ZnSO4 (colourless) + Cu (brown deposit). Zn > Cu in reactivity!"
        1 -> "Fe + CuSO4 (blue) -> FeSO4 (pale green) + Cu (reddish-brown coating). Fe > Cu!"
        else -> "Cu + FeSO4 -> No Reaction (Cu is less reactive than Fe and Zn)."
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            metals.forEachIndexed { idx, label ->
                FilterChip(selected = metalIdx == idx, onClick = { metalIdx = idx }, label = { Text(label) })
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (reactsWithCuSO4) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
        ) {
            Text(
                text = if (isKannada) "$explanation (Step ${stepIndex + 1})" else explanation,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            )
        }
    }
}

@Composable
private fun CarbonFunctionalLab(isKannada: Boolean) {
    var carbons by remember { mutableIntStateOf(2) }
    var groupIdx by remember { mutableIntStateOf(0) }
    val groups = listOf("Alcohol (-OH)", "Aldehyde (-CHO)", "Carboxylic Acid (-COOH)")
    val prefix = listOf("Meth", "Eth", "Prop", "But")[carbons - 1]
    val name = when (groupIdx) {
        0 -> "${prefix}anol (C${carbons}H${2 * carbons + 1}OH)"
        1 -> "${prefix}anal"
        else -> "${prefix}anoic acid"
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..4).forEach { c ->
                FilterChip(selected = carbons == c, onClick = { carbons = c }, label = { Text("C$c") })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            groups.forEachIndexed { idx, label ->
                FilterChip(selected = groupIdx == idx, onClick = { groupIdx = idx }, label = { Text(label) })
            }
        }
        Text(
            text = if (isKannada) "IUPAC ಹೆಸರು: $name" else "IUPAC Name: $name",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun HeartCirculationLab(animPhase: Float, stepIndex: Int, showLabels: Boolean, isKannada: Boolean) {
    val beatScale = 1f + 0.05f * sin(animPhase * 2f)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(175.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val w = 130f * beatScale
            val h = 120f * beatScale
            // Right half (deoxygenated blue)
            drawRect(Color(0xFF3B82F6), Offset(cx - w / 2f, cy - h / 2f), Size(w / 2f, h))
            // Left half (oxygenated red)
            drawRect(Color(0xFFEF4444), Offset(cx, cy - h / 2f), Size(w / 2f, h))
            // Septum divider
            drawLine(Color.White, Offset(cx, cy - h / 2f), Offset(cx, cy + h / 2f), strokeWidth = 6f)
            // Atrium-ventricle divider
            drawLine(Color.White, Offset(cx - w / 2f, cy), Offset(cx + w / 2f, cy), strokeWidth = 4f)
        }
        if (showLabels) {
            val steps = listOf(
                "1. Vena Cava -> Right Atrium -> Right Ventricle (Deoxygenated)",
                "2. Pulmonary Artery -> Lungs (Oxygenation)",
                "3. Pulmonary Vein -> Left Atrium -> Left Ventricle -> Aorta (Systemic)"
            )
            Text(
                text = if (isKannada) "ಹಂತ: ${steps[stepIndex % steps.size]}" else steps[stepIndex % steps.size],
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun NephronSimulationLab(animPhase: Float, stepIndex: Int, showLabels: Boolean) {
    val stages = listOf(
        "1. Glomerular Ultrafiltration inside Bowman's Capsule",
        "2. Selective Reabsorption (Glucose, Amino acids, Water) along Tubule",
        "3. Urine Collection into Collecting Duct -> Ureter"
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(155.dp)) {
            val p = Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.35f)
                lineTo(size.width * 0.45f, size.height * 0.35f)
                lineTo(size.width * 0.45f, size.height * 0.80f)
                lineTo(size.width * 0.62f, size.height * 0.80f)
                lineTo(size.width * 0.62f, size.height * 0.25f)
                lineTo(size.width * 0.82f, size.height * 0.25f)
                lineTo(size.width * 0.82f, size.height * 0.90f)
            }
            drawPath(p, color = Color(0xFFD97706), style = Stroke(width = 10f))
            drawCircle(Color(0xFFDC2626), 24f, Offset(size.width * 0.20f, size.height * 0.35f))
            val pulseX = size.width * (0.20f + 0.60f * (animPhase / (2f * PI.toFloat())))
            drawCircle(Color(0xFF16A34A), 8f, Offset(pulseX, size.height * 0.35f))
        }
        if (showLabels) {
            Text(stages[stepIndex % stages.size], style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ReflexArcLab(animPhase: Float, stepIndex: Int, showLabels: Boolean) {
    val nodes = listOf("Receptor (Skin)", "Sensory Neuron", "Spinal Cord Relay", "Motor Neuron", "Effector Muscle")
    val activeNode = (stepIndex + (animPhase * 1.5f).toInt()) % nodes.size
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            val stepW = size.width / (nodes.size + 1)
            val cy = size.height / 2f
            drawLine(Color(0xFF64748B), Offset(stepW, cy), Offset(stepW * nodes.size, cy), strokeWidth = 5f)
            nodes.indices.forEach { i ->
                val x = stepW * (i + 1)
                drawCircle(
                    color = if (i == activeNode) Color(0xFFDC2626) else Color(0xFF0F766E),
                    radius = if (i == activeNode) 18f else 12f,
                    center = Offset(x, cy)
                )
            }
        }
        if (showLabels) {
            Text("Active Impulse Stage: ${nodes[activeNode]}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun EnergyPyramidLab(showLabels: Boolean, isKannada: Boolean) {
    var solarInput by remember { mutableFloatStateOf(10000f) }
    val producer = solarInput * 0.01f
    val primary = producer * 0.10f
    val secondary = primary * 0.10f
    val tertiary = secondary * 0.10f
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(155.dp)) {
            val widths = listOf(0.86f, 0.66f, 0.46f, 0.28f)
            val colors = listOf(Color(0xFF16A34A), Color(0xFF0D9488), Color(0xFF2563EB), Color(0xFFD97706))
            widths.forEachIndexed { idx, wFrac ->
                val y = size.height - (idx + 1) * 34f
                val w = size.width * wFrac
                drawRect(colors[idx], Offset((size.width - w) / 2f, y), Size(w, 28f))
            }
        }
        if (showLabels) {
            Text(
                text = if (isKannada) {
                    "ಉತ್ಪಾದಕರು: ${producer.toInt()} J -> ಪ್ರಾಥಮಿಕ: ${"%.1f".format(primary)} J -> ದ್ವಿತೀಯಕ: ${"%.2f".format(secondary)} J -> ತೃತೀಯಕ: ${"%.3f".format(tertiary)} J"
                } else {
                    "Producers: ${producer.toInt()} J -> Primary: ${"%.1f".format(primary)} J -> Secondary: ${"%.2f".format(secondary)} J -> Tertiary: ${"%.3f".format(tertiary)} J"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(value = solarInput, onValueChange = { solarInput = it }, valueRange = 5000f..50000f)
    }
}
