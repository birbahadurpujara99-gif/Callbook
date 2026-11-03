package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CallbookViewModel
import com.example.ui.MainFeedScreen
import com.example.ui.components.AuthScreen
import com.example.ui.components.RegistrationScreen
import com.example.ui.theme.CallbookTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private val viewModel: CallbookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CallbookTheme {
                AppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AppRoot(viewModel: CallbookViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.currentUser == null -> {
            AuthScreen(
                language = uiState.language,
                onAuthSuccess = {
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        viewModel.loadUserProfile(user.uid)
                    }
                },
                onToggleLanguage = {
                    viewModel.toggleLanguage()
                }
            )
        }

        uiState.isProfileLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        !uiState.isProfileRegistered -> {
            RegistrationScreen(
                user = uiState.currentUser!!,
                language = uiState.language,
                isSaving = uiState.isSavingProfile,
                onSaveProfile = { name, phone, photoUrl, bio ->
                    viewModel.saveProfile(name, phone, photoUrl, bio)
                },
                onToggleLanguage = {
                    viewModel.toggleLanguage()
                },
                onProcessImage = { uri ->
                    viewModel.processImageUri(viewModel.getApplication(), uri)
                },
                onSignOut = {
                    Firebase.auth.signOut()
                }
            )
        }

        else -> {
            MainFeedScreen(viewModel = viewModel)
        }
    }
}
