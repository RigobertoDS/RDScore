package com.rigobertods.rdscore.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.data.SessionManager
import com.rigobertods.rdscore.features.auth.data.LoginRequest
import com.rigobertods.rdscore.features.auth.data.RegisterRequest
import com.rigobertods.rdscore.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(username: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = LoginRequest(username, pass)
            authRepository.login(request)
                .onSuccess { response ->
                    sessionManager.saveTokens(response.accessToken, response.refreshToken)
                    // Prioritize successCode for translation, fallback to message
                    onResult(true, response.successCode ?: response.message)
                }
                .onFailure { error ->
                    val errorMessage = if (error is com.rigobertods.rdscore.core.network.BackendException) {
                         error.code ?: error.message
                    } else {
                         error.message ?: "Unknown Error"
                    }
                    onResult(false, errorMessage)
                }
            _isLoading.value = false
        }
    }

    fun register(username: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = RegisterRequest(username, pass, email)
            authRepository.register(request)
                .onSuccess { response ->
                    onResult(true, response.successCode ?: response.message)
                }
                .onFailure { error ->
                    val errorMessage = if (error is com.rigobertods.rdscore.core.network.BackendException) {
                         error.code ?: error.message
                    } else {
                         error.message ?: "Unknown Error"
                    }
                    onResult(false, errorMessage)
                }
            _isLoading.value = false
        }
    }

    // Removed checkSession() to avoid blocking Main Thread. 
    // UI should observe sessionManager.isUserLoggedIn directly or via a Flow in ViewModel if needed.
    val isUserLoggedIn = sessionManager.isUserLoggedIn
}
