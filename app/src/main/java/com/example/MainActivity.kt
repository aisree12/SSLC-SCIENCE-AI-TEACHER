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
                    // Short branded hand-off while Firebase session state and the first screen settle.
                    delay(1100)
                    showStartupScreen = false
                }

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
                    ScienceStartupScreen()
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


@Composable
private fun ScienceStartupScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(28.dp)
        ) {
            Text(
                text = "SSLC",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "SCIENCE TEACHER",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Karnataka Class 10 Digital Science Classroom",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(28.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Preparing your classroom…",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
