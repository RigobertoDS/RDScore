package com.rigobertods.rdscore.features.equipo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rigobertods.rdscore.features.partidos.data.DatosEquipo
import com.rigobertods.rdscore.features.equipo.data.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleEquipoViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _datosEquipo = MutableStateFlow(DatosEquipo())
    val datosEquipo: StateFlow<DatosEquipo> = _datosEquipo.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Tab state preserved across navigation
    private val _selectedTabIndex = MutableStateFlow(1) // Default: Clasificación (index 1)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()
    
    fun setSelectedTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
    
    // League selection preserved across navigation
    private val _selectedLeagueId = MutableStateFlow<Int?>(null) // null = use initial
    val selectedLeagueId: StateFlow<Int?> = _selectedLeagueId.asStateFlow()
    
    fun setSelectedLeagueId(id: Int) {
        _selectedLeagueId.value = id
    }

    fun obtenerDatosEquipo(idEquipo: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            teamRepository.getDatosEquipo(idEquipo)
                .onSuccess { response ->
                    _datosEquipo.value = response.datosEquipo
                }
                .onFailure { error ->
                    onError(error.message ?: "Error al obtener datos del equipo")
                }
            _isLoading.value = false
        }
    }
}
