package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebase.attemptAutoSignIn
import com.example.firebase.authStateFlow
import com.example.firebase.configureAppCheck
import com.example.ui.navigation.ScienceTeacherApp
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ScienceCinematicIntroScreen
import com.example.ui.theme.ScienceTeacherTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // App Check MUST initialize before setContent and any Firebase AI/Firestore calls.
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            ScienceTeacherTheme {
                val scope = rememberCoroutineScope()
                val credentialManager = remember { CredentialManager.create(this@MainActivity) }
                val currentUser by remember { Firebase.auth.authStateFlow() }
                    .collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)
                var authErrorMessage by remember { mutableStateOf<String?>(null) }
                var showStartupScreen by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    if (Firebase.auth.currentUser == null) {
                        attemptAutoSignIn(
                            context = this@MainActivity,
                            credentialManager = credentialManager,
                            onAuthSuccess = { authErrorMessage = null },
                            onUnauthenticated = {},
                            scope = scope
                        )
                    }
                }

                val activeUser = currentUser
                if (showStartupScreen) {
                    ScienceCinematicIntroScreen(
                        studentName = activeUser?.displayName.orEmpty(),
                        onEnterClassroom = { showStartupScreen = false }
                    )
                } else if (activeUser == null) {
                    AuthScreen(
                        authErrorMessage = authErrorMessage,
                        onAuthSuccess = { authErrorMessage = null },
                        onAuthError = { message -> authErrorMessage = message }
                    )
                } else {
                    key(activeUser.uid) {
                        ScienceTeacherApp(
                            userId = activeUser.uid,
                            userDisplayName = activeUser.displayName.orEmpty(),
                            userEmail = activeUser.email.orEmpty()
                        )
                    }
                }
            }
        }
    }
}

