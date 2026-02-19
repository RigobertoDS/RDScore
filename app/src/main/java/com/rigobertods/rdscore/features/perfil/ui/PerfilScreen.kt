package com.rigobertods.rdscore.features.perfil.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.core.common.UiState
import com.rigobertods.rdscore.ui.util.traducirMensaje
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    onNavigateToEditProfile: (String, String) -> Unit,
    onNavigateToEditPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val username = uiState.data?.username ?: ""
    val email = uiState.data?.email ?: ""
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val showSnackbar: (String) -> Unit = { message ->
        val translatedMessage = traducirMensaje(context, message)
        coroutineScope.launch { snackbarHostState.showSnackbar(translatedMessage) }
    }

    // Initial Load
    LaunchedEffect(Unit) {
        viewModel.obtenerPerfil()
    }
    
    // Show errors
    LaunchedEffect(error) {
        error?.let {
            showSnackbar(it)
            viewModel.clearError()
        }
    }

    PantallaPerfil(
        username = username,
        email = email,
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onEditClick = { onNavigateToEditProfile(username, email) },
        onChangePasswordClick = onNavigateToEditPassword,
        onLogoutClick = { 
            viewModel.logout() 
        },
        onDeleteAccountClick = { 
            viewModel.eliminarCuenta(onResult = showSnackbar)
        },
        onNavigateBack = onNavigateBack
    )
}

