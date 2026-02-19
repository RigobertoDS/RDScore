package com.rigobertods.rdscore.features.perfil.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.core.common.UiState

@Composable
fun EditarPerfilScreen(
    initialUsername: String,
    initialEmail: String,
    onNavigateBack: () -> Unit,
    onProfileUpdated: () -> Unit,
    viewModel: EditarPerfilViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message
    val isSuccess = (uiState as? UiState.Success)?.data != null

    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onProfileUpdated()
        }
    }

    LaunchedEffect(error) {
            error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
            }
    }


    PantallaEditarPerfil(
        initialUsername = initialUsername,
        initialEmail = initialEmail,
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onGuardarClick = { username, email ->
            viewModel.editarPerfil(username, email)
        },
        onCancelarClick = onNavigateBack,
        onNavigateBack = onNavigateBack
    )
}
