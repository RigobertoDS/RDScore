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
fun ResetPasswordScreen(
    email: String,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RestablecerPasswordViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val defaultErrorMsg = stringResource(R.string.error)
    val emailResentMsg = stringResource(R.string.msg_email_sent)
    val resendErrorMsg = stringResource(R.string.error_email_send_failed)

    PantallaRestablecerPassword(
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onRestablecerClick = { code, newPassword ->
            viewModel.restablecerPassword(code, newPassword) { success, message ->
                if (success) {
                    coroutineScope.launch {
                        onNavigateToLogin()
                    }
                } else {
                    coroutineScope.launch {
                        val translatedError = traducirMensaje(
                            context,
                            message ?: defaultErrorMsg
                        )
                        snackbarHostState.showSnackbar(translatedError)
                    }
                }
            }
        },
        onReenviarClick = {
            viewModel.reenviarEmail(email) { success, message ->
                coroutineScope.launch {
                    val translatedMsg = traducirMensaje(
                        context,
                        message ?: if (success) emailResentMsg else resendErrorMsg
                    )
                    snackbarHostState.showSnackbar(translatedMsg)
                }
            }
        },
        onNavigateBack = onNavigateBack
    )
}

