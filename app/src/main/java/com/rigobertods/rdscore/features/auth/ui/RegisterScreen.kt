package com.rigobertods.rdscore.features.auth.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.ui.util.traducirMensaje
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Resources
    val successMessage = stringResource(R.string.success_account_created)
    val defaultErrorMessage = stringResource(R.string.error)

    PantallaRegistro(
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onRegistroClick = { username, email, password ->
            viewModel.register(username, email, password) { success, message ->
                if (success) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(successMessage)
                    }
                    // Delay slighty or just navigate
                    onNavigateToLogin() // Original logic went to Log in (InicioActivity)
                } else {
                    coroutineScope.launch {
                        val translatedError = traducirMensaje(
                            context,
                            message ?: defaultErrorMessage
                        )
                        snackbarHostState.showSnackbar(translatedError)
                    }
                }
            }
        },
        onYaTienesCuentaClick = onNavigateToLogin,
        onNavigateBack = onNavigateBack
    )
}

