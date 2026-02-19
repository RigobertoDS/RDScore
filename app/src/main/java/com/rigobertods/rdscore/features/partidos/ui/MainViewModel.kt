package com.rigobertods.rdscore.features.partidos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.PartidosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.rigobertods.rdscore.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.rigobertods.rdscore.features.ligas.data.LeagueRepository
import com.rigobertods.rdscore.features.ligas.data.Liga

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PartidosRepository,
    leagueRepository: LeagueRepository
) : ViewModel() {

    private val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val _uiState = MutableStateFlow<UiState<Map<LocalDate, List<Partido>>>>(UiState.Success(emptyMap()))
    val uiState: StateFlow<UiState<Map<LocalDate, List<Partido>>>> = _uiState.asStateFlow()
    
    val leagues: StateFlow<Map<Int, Liga>> = leagueRepository.leagues

    fun obtenerPartidosDeLaFecha(fecha: LocalDate) {
        val currentData = _uiState.value.data ?: emptyMap()
        
        // If already loaded in memory for this date, skip
        if (currentData.containsKey(fecha)) return

        viewModelScope.launch {
            val fechaStringParaApi = fecha.format(backendFormatter)
            
            // 1. Intentar cargar desde caché para respuesta instantánea
            val cachedResult = repository.getPartidosPorFechaCached(fechaStringParaApi)
            
            if (!cachedResult.isNullOrEmpty()) {
                val latestData = _uiState.value.data ?: emptyMap()
                val newData = latestData + (fecha to cachedResult)
                _uiState.value = UiState.Success(newData)
                
                // 2. Refrescar en background para obtener predicciones/resultados actualizados
                refreshPartidosInBackground(fecha, fechaStringParaApi)
                prefetchFechasAdyacentes(fecha)
            } else {
                // 3. Si no hay caché, carga normal con Loading
                val latestBeforeLoad = _uiState.value.data ?: emptyMap()
                _uiState.value = UiState.Loading(latestBeforeLoad)
                
                realizarCargaDesdeRed(fecha, fechaStringParaApi)
            }
        }
    }

    private fun refreshPartidosInBackground(fecha: LocalDate, fechaString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPartidosPorFecha(fechaString)
                .onSuccess { partidos ->
                    val latestData = _uiState.value.data ?: emptyMap()
                    val newData = latestData + (fecha to partidos)
                    _uiState.value = UiState.Success(newData)
                }
                // Falla silenciosa en background
        }
    }

    private suspend fun realizarCargaDesdeRed(fecha: LocalDate, fechaString: String) {
        repository.getPartidosPorFecha(fechaString)
            .onSuccess { partidos ->
                val latestData = _uiState.value.data ?: emptyMap()
                val newData = latestData + (fecha to partidos)
                _uiState.value = UiState.Success(newData)
                prefetchFechasAdyacentes(fecha)
            }
            .onFailure { exception ->
                val mensajeError = when (exception) {
                    is java.io.IOException -> "Error de conexión: ${exception.message}"
                    else -> "Ocurrió un error: ${exception.message}"
                }
                val latestData = _uiState.value.data ?: emptyMap()
                _uiState.value = UiState.Error(mensajeError, latestData)
            }
    }

    /**
     * Precarga silenciosa de los días anterior y siguiente.
     * Se ejecuta después de cargar exitosamente la fecha actual.
     */
    private fun prefetchFechasAdyacentes(fecha: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            // Esperar a que la UI se estabilice
            delay(500)
            
            val currentData = _uiState.value.data ?: emptyMap()
            val fechasAPrefetch = listOf(
                fecha.minusDays(1),
                fecha.plusDays(1)
            )
            
            fechasAPrefetch.forEach { f ->
                if (!currentData.containsKey(f)) {
                    repository.prefetchPartidosPorFecha(f.format(backendFormatter))
                }
            }
        }
    }

    fun clearError() {
        val currentData = _uiState.value.data ?: emptyMap()
        // Reset to Success clearing the error
        _uiState.value = UiState.Success(currentData)
    }
}
