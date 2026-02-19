package com.rigobertods.rdscore.features.resumen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.partidos.data.Resumen
import com.rigobertods.rdscore.features.partidos.data.ResumenMercado
import com.rigobertods.rdscore.features.resumen.data.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ResumenViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _precision = MutableStateFlow<Resumen?>(null)
    val precision: StateFlow<Resumen?> = _precision.asStateFlow()

    private val _precisionMercado = MutableStateFlow<ResumenMercado?>(null)
    val precisionMercado: StateFlow<ResumenMercado?> = _precisionMercado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun obtenerPrecision() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getPrecision()
            result.onSuccess { resumen ->
                _precision.value = resumen
            }.onFailure { error ->
                _error.value = error.message ?: "Error desconocido"
            }
            _isLoading.value = false
        }
    }

    fun obtenerPrecisionMercado() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getPrecisionPorMercado()
            result.onSuccess { resumen ->
                _precisionMercado.value = resumen
            }.onFailure { error ->
                _error.value = error.message ?: "Error desconocido"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
