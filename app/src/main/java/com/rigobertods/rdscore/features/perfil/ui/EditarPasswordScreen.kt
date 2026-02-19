package com.rigobertods.rdscore.features.perfil.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.core.common.UiState
import com.rigobertods.rdscore.ui.util.traducirMensaje

@Composable
fun EditarPasswordScreen(
    onNavigateBack: () -> Unit,
    onPasswordUpdated: () -> Unit,
    viewModel: EditarPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message
    val isSuccess = (uiState as? UiState.Success)?.data != null

    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onPasswordUpdated()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            val translatedError = traducirMensaje(context, it)
            snackbarHostState.showSnackbar(translatedError)
            viewModel.clearError()
        }
    }

    PantallaEditarPassword(
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onGuardarClick = { contrasenaActual, nuevaContrasena ->
            viewModel.editarPassword(contrasenaActual, nuevaContrasena)
        },
        onCancelarClick = onNavigateBack,
        onNavigateBack = onNavigateBack
    )
}

