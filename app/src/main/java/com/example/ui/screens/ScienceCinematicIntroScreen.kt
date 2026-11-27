package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScienceCinematicIntroScreen(
    studentName: String,
    onEnterClassroom: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onEnterClassroom() }

    var showCrest by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showBadges by remember { mutableStateOf(false) }
    var showDeveloper by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(false) }
    var autoAdvancePaused by remember { mutableStateOf(false) }
    var countdownProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        showCrest = true
        delay(140)
        showTitle = true
        delay(160)
        showBadges = true
        delay(180)
        showDeveloper = true
        delay(160)
        showAction = true
    }

    LaunchedEffect(autoAdvancePaused) {
        if (autoAdvancePaused) return@LaunchedEffect
        val totalDurationMs = 5500L
        val stepMs = 50L
        var elapsed = (countdownProgress * totalDurationMs).toLong()
        while (elapsed < totalDurationMs && !autoAdvancePaused) {
            delay(stepMs)
            elapsed += stepMs
            countdownProgress = (elapsed.toFloat() / totalDurationMs).coerceIn(0f, 1f)
        }
        if (!autoAdvancePaused && countdownProgress >= 0.99f) {
            onEnterClassroom()
        }
    }

    val smoothProgress by animateFloatAsState(
        targetValue = countdownProgress,
        animationSpec = tween(durationMillis = 80, easing = LinearEasing),
        label = "introProgress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "cinematicMotion")
    val colorShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorShift"
    )
    val rotationDeg by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "crestRotation"
    )
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sineWavePhase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF312E81),
                        Color(0xFF0F766E),
                        Color(0xFF7C2D12)
                    )
                )
            )
            .testTag("cinematic_intro_screen")
    ) {
        // Animated colorful math bokeh, rotating geometric compass ring, and flowing sine curve
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val orb1 = Offset(w * (0.22f + 0.14f * colorShift), h * (0.18f + 0.06f * colorShift))
            val orb2 = Offset(w * (0.82f - 0.15f * colorShift), h * (0.34f + 0.12f * colorShift))
            val orb3 = Offset(w * (0.35f + 0.25f * colorShift), h * (0.78f - 0.08f * colorShift))

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x88F59E0B), Color.Transparent),
                    center = orb1,
                    radius = w * 0.45f
                ),
                radius = w * 0.45f,
                center = orb1
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x88EC4899), Color.Transparent),
                    center = orb2,
                    radius = w * 0.42f
                ),
                radius = w * 0.42f,
                center = orb2
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x8810B981), Color.Transparent),
                    center = orb3,
                    radius = w * 0.48f
                ),
                radius = w * 0.48f,
                center = orb3
            )

            // Rotating mathematical geometry lattice behind the crest
            val crestCenter = Offset(w * 0.5f, h * 0.20f)
            val orbitRadius = (minOf(w, h) * 0.22f).coerceAtLeast(90f)
            rotate(degrees = rotationDeg, pivot = crestCenter) {
                drawCircle(
                    color = Color(0x55FBBF24),
                    radius = orbitRadius,
                    center = crestCenter,
                    style = Stroke(width = 3f)
                )
                val hexPath = Path()
                for (i in 0 until 6) {
                    val angle = i * PI / 3.0
                    val px = crestCenter.x + (orbitRadius * 0.86f * cos(angle)).toFloat()
                    val py = crestCenter.y + (orbitRadius * 0.86f * sin(angle)).toFloat()
                    if (i == 0) hexPath.moveTo(px, py) else hexPath.lineTo(px, py)
                }
                hexPath.close()
                drawPath(
                    path = hexPath,
                    color = Color(0x6638BDF8),
                    style = Stroke(width = 2.5f)
                )
            }

            // Flowing trigonometric sine wave along the lower third
            val wavePath = Path()
            val waveY = h * 0.86f
            val amp = h * 0.032f
            val step = 12f
            var x = 0f
            while (x <= w + step) {
                val y = waveY + sin((x / w) * 4f * PI.toFloat() + wavePhase) * amp
                if (x == 0f) wavePath.moveTo(x, y) else wavePath.lineTo(x, y)
                x += step
            }
            drawPath(
                path = wavePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0x9938BDF8), Color(0x99FBBF24), Color(0x99F472B6))
                ),
                style = Stroke(width = 5f, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .widthIn(max = 560.dp)
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            AnimatedVisibility(
                visible = showCrest,
                enter = scaleIn(
                    initialScale = 0.72f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(tween(350))
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1E1B4B).copy(alpha = 0.88f),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFBBF24), Color(0xFF34D399), Color(0xFFF472B6))
                        )
                    ),
                    shadowElevation = 12.dp,
                    modifier = Modifier.size(92.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "SSLC Science AI Crest",
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showTitle,
                enter = slideInVertically(initialOffsetY = { it / 3 }) + fadeIn(tween(400))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = Color(0xFFFBBF24).copy(alpha = 0.18f),
                        shape = RoundedCornerShape(999.dp),
                        border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.65f))
                    ) {
                        Text(
                            text = "KARNATAKA CLASS 10 • SSLC SCIENCE AI",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            color = Color(0xFFFDE68A),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                    Text(
                        text = "Welcome, ${studentName.ifBlank { "SSLC Scholar" }}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Your Karnataka SSLC Science learning classroom, practical lab and board-exam companion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE2E8F0),
                        textAlign = TextAlign.Center
                    )
                }
            }

            AnimatedVisibility(
                visible = showBadges,
                enter = slideInVertically(initialOffsetY = { it / 3 }) + fadeIn(tween(420))
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ColorfulHighlightChip(
                        text = "2026–27 Textbook Linked",
                        accent = Color(0xFF34D399)
                    )
                    ColorfulHighlightChip(
                        text = "AI Science Teacher",
                        accent = Color(0xFF38BDF8)
                    )
                    ColorfulHighlightChip(
                        text = "Interactive Science Labs",
                        accent = Color(0xFFF472B6)
                    )
                    ColorfulHighlightChip(
                        text = "English • ಕನ್ನಡ",
                        accent = Color(0xFFFBBF24)
                    )
                }
            }

            AnimatedVisibility(
                visible = showDeveloper,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(tween(450))
            ) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFF0B1120).copy(alpha = 0.86f),
                    border = BorderStroke(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFBBF24), Color(0xFFEC4899), Color(0xFF38BDF8), Color(0xFF10B981))
                        )
                    ),
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { autoAdvancePaused = true }
                        .testTag("developer_credit_card")
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Code,
                                contentDescription = "Developer",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "DEVELOPED & ARCHITECTED BY",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color(0xFFFDE68A)
                            )
                        }

                        Text(
                            text = "SVANAM SOFTS",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = Color(0xFF10B981).copy(alpha = 0.16f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Call,
                                        contentDescription = "Phone",
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "6360953674",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD1FAE5)
                                    )
                                }
                            }

                            Surface(
                                color = Color(0xFFEC4899).copy(alpha = 0.18f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = "City",
                                        tint = Color(0xFFF472B6),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "VIJAYAPUR",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFCE7F3)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Built for Karnataka Class 10 Students • Tap card to hold view",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showAction,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(tween(450))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = { smoothProgress },
                        color = Color(0xFFFBBF24),
                        trackColor = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (autoAdvancePaused) {
                            Icon(
                                imageVector = Icons.Filled.PauseCircle,
                                contentDescription = null,
                                tint = Color(0xFFFDE68A),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = if (autoAdvancePaused) {
                                "Auto-launch paused — press Enter Science Classroom when ready"
                            } else {
                                "Opening your Science classroom automatically..."
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    Button(
                        onClick = onEnterClassroom,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFBBF24),
                            contentColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("enter_classroom_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Enter Classroom",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorfulHighlightChip(
    text: String,
    accent: Color
) {
    Surface(
        color = accent.copy(alpha = 0.16f),
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.55f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
        )
    }
}
