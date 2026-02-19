package com.rigobertods.rdscore.features.perfil.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.data.SessionManager
import com.rigobertods.rdscore.features.perfil.data.UserRepository
import com.rigobertods.rdscore.features.perfil.data.GetProfileResponse
import com.rigobertods.rdscore.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GetProfileResponse>>(UiState.Success(GetProfileResponse(0, "", "", "")))
    val uiState: StateFlow<UiState<GetProfileResponse>> = _uiState.asStateFlow()

    fun obtenerPerfil() {
        val currentData = _uiState.value.data
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading(currentData)
            userRepository.getProfile()
                .onSuccess { profile ->
                    _uiState.value = UiState.Success(profile)
                }
                .onFailure { error ->
                    val errorMessage = if (error is com.rigobertods.rdscore.core.network.BackendException && error.code != null) {
                        error.code
                    } else {
                        error.message ?: "Error al obtener perfil"
                    }
                    _uiState.value = UiState.Error(errorMessage, currentData)
                }
        }
    }

    fun eliminarCuenta(onResult: (String) -> Unit) {
        val currentData = _uiState.value.data
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading(currentData)
            userRepository.deleteAccount()
                .onSuccess { _ ->
                    logout()
                }
                .onFailure { error ->
                    val msg = if (error is com.rigobertods.rdscore.core.network.BackendException && error.code != null) {
                        error.code
                    } else {
                        error.message ?: "Error al eliminar cuenta"
                    }
                    onResult(msg)
                    _uiState.value = UiState.Error(msg, currentData)
                }
        }
    }

    fun clearError() {
        val currentData = _uiState.value.data ?: GetProfileResponse(0, "", "", "")
        _uiState.value = UiState.Success(currentData)
    }

    fun logout() {
        sessionManager.logout()
    }
}
