package com.rigobertods.rdscore.features.auth.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.core.common.LanguageManager
import com.rigobertods.rdscore.ui.util.traducirMensaje
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

// Entry point to access LanguageManager in Composable
@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface LanguageManagerEntryPoint {
    fun languageManager(): LanguageManager
}

@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecover: () -> Unit,
    snackbarMessage: String? = null,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    
    // Get LanguageManager via entry point
    val languageManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            LanguageManagerEntryPoint::class.java
        ).languageManager()
    }
    val currentLanguage = remember { languageManager.getEffectiveLanguage() }
    
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            onNavigateToMain()
        }
    }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Pre-resolve strings to avoid using LocalContext.current in callbacks
    val defaultErrorMessage = stringResource(R.string.error)
    val errorCompleteFieldsMessage = stringResource(R.string.error_complete_fields)

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage != null) {
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    PantallaInicio(
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        snackbarMessage = null, // Handled by LaunchedEffect above
        currentLanguage = currentLanguage,
        onLanguageChange = { languageCode ->
            // Save to LanguageManager (DataStore + SharedPrefs)
            languageManager.setLanguage(languageCode)
            // Recreate activity to apply new locale fully
            (context as? android.app.Activity)?.recreate()
        },
        onIniciarSesionClick = { usuario, password ->
            if (usuario.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(usuario, password) { success, error ->
                    if (success) {
                        onNavigateToMain()
                    } else {
                        coroutineScope.launch {
                            val translatedError = traducirMensaje(
                                context, 
                                error ?: defaultErrorMessage
                            )
                            snackbarHostState.showSnackbar(translatedError)
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorCompleteFieldsMessage)
                }
            }
        },
        onRecuperarClick = onNavigateToRecover,
        onRegistrarClick = onNavigateToRegister
    )
}



