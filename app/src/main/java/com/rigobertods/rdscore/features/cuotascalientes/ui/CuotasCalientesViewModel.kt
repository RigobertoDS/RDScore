package com.rigobertods.rdscore.features.cuotascalientes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.partidos.data.CuotaCaliente
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.resumen.data.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CuotasCalientesViewModel @Inject constructor(
    private val repository: StatsRepository,
    private val partidosRepository: com.rigobertods.rdscore.features.partidos.data.PartidosRepository
) : ViewModel() {

    private val _partidos = MutableStateFlow<List<Partido>>(emptyList())
    val partidos: StateFlow<List<Partido>> = _partidos.asStateFlow()

    private val _cuotasCalientes = MutableStateFlow<List<CuotaCaliente>>(emptyList())
    val cuotasCalientes: StateFlow<List<CuotaCaliente>> = _cuotasCalientes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun obtenerCuotasCalientes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getCuotasCalientes()
            result.onSuccess { response ->
                _partidos.value = response.partidosList
                _cuotasCalientes.value = response.cuotasCalientes
                
                // Cachear partidos en Room para navegación por ID
                partidosRepository.cachePartidos(response.partidosList)
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
