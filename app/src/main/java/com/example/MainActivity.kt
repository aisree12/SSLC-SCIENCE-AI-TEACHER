package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
                if (activeUser == null) {
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
