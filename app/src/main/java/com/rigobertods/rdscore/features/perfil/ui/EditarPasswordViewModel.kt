package com.rigobertods.rdscore.features.perfil.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.perfil.data.EditPasswordRequest
import com.rigobertods.rdscore.features.perfil.data.UserRepository
import com.rigobertods.rdscore.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditarPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit?>>(UiState.Success(null))
    val uiState: StateFlow<UiState<Unit?>> = _uiState.asStateFlow()

    fun editarPassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading(null)
            val request = EditPasswordRequest(oldPassword = oldPass, newPassword = newPass)
            userRepository.editPassword(request)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }
                .onFailure { error ->
                    val errorMessage = if (error is com.rigobertods.rdscore.core.network.BackendException && error.code != null) {
                        error.code
                    } else {
                        error.message ?: "Error al cambiar contraseña"
                    }
                    _uiState.value = UiState.Error(errorMessage, null)
                }
        }
    }

    fun clearError() {
        _uiState.value = UiState.Success(null)
    }
}
