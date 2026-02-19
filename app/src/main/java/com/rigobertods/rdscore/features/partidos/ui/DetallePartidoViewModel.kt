package com.rigobertods.rdscore.features.partidos.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.PartidosRepository
import com.rigobertods.rdscore.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePartidoViewModel @Inject constructor(
    private val repository: PartidosRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val partidoId: Int = savedStateHandle["partidoId"] ?: 0

    private val _uiState = MutableStateFlow<UiState<Partido>>(UiState.Loading())
    val uiState: StateFlow<UiState<Partido>> = _uiState.asStateFlow()

    init {
        cargarPartido()
    }

    private fun cargarPartido() {
        if (partidoId == 0) {
            _uiState.value = UiState.Error("ID de partido inválido")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading()
            
            val partidoFromCache = repository.getPartidoById(partidoId)

            if (partidoFromCache != null) {
                // Solo enriquecer si el partido NO ha finalizado
                // Para partidos pasados, el resultado es lo importante, no la predicción
                val partidoFinal = if (partidoFromCache.estado == "FT") {
                    // Partido finalizado: mostrar directamente
                    partidoFromCache
                } else {
                    // Partido futuro: intentar enriquecer con predicciones
                    repository.enriquecerPartidoDesdeRed(partidoFromCache)
                }
                _uiState.value = UiState.Success(partidoFinal)
            } else {
                _uiState.value = UiState.Error("No se encontró el partido en caché")
            }
        }
    }

}
