package com.rigobertods.rdscore.features.auth.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.ui.util.traducirMensaje
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreen(
    onNavigateToResetPassword: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RecuperarCuentaViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val defaultSuccessMsg = stringResource(R.string.msg_email_sent)
    val defaultErrorMsg = stringResource(R.string.error)
    val completeFieldsMsg = stringResource(R.string.error_complete_fields)

    PantallaRecuperarCuenta(
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onMandarEmailClick = { email ->
            if (email.isNotEmpty()) {
                viewModel.mandarEmailRecuperacion(email) { success, message ->
                    val translatedMessage = traducirMensaje(
                        context,
                        message ?: if (success) defaultSuccessMsg else defaultErrorMsg
                    )
                    if (success) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(translatedMessage)
                            onNavigateToResetPassword(email)
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(translatedMessage)
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(completeFieldsMsg)
                }
            }
        },
        onNavigateBack = onNavigateBack
    )
}

