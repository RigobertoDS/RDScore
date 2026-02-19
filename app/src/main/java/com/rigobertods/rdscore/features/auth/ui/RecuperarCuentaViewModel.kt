package com.rigobertods.rdscore.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.auth.data.ForgotPasswordRequest
import com.rigobertods.rdscore.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecuperarCuentaViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun mandarEmailRecuperacion(email: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = ForgotPasswordRequest(email)
            authRepository.forgotPassword(request)
                .onSuccess { response ->
                    onResult(true, response.successCode ?: response.message)
                }
                .onFailure { error ->
                    val errorMessage = if (error is com.rigobertods.rdscore.core.network.BackendException && error.code != null) {
                        error.code
                    } else {
                        error.message
                    }
                    onResult(false, errorMessage)
                }
            _isLoading.value = false
        }
    }
}
