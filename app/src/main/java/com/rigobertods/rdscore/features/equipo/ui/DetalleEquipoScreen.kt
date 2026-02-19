package com.rigobertods.rdscore.features.equipo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun DetalleEquipoScreen(
    idEquipo: Int,
    idLiga: Int,
    onNavigateBack: () -> Unit,
    onEquipoClick: (idEquipo: Int, idLiga: Int) -> Unit = { _, _ -> },
    onPartidoClick: (Int) -> Unit = {},
    viewModel: DetalleEquipoViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val datosEquipo by viewModel.datosEquipo.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val selectedLeagueId by viewModel.selectedLeagueId.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.obtenerDatosEquipo(idEquipo) { error ->
            coroutineScope.launch { snackbarHostState.showSnackbar(error) }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        PantallaDetalleEquipo(
            idEquipo = idEquipo,
            idLiga = idLiga,
            datosEquipo = datosEquipo,
            isLoading = isLoading,
            selectedTabIndex = selectedTabIndex,
            onTabChanged = { viewModel.setSelectedTabIndex(it) },
            selectedLeagueId = selectedLeagueId ?: idLiga,
            onLeagueChanged = { viewModel.setSelectedLeagueId(it) },
            onNavigateBack = onNavigateBack,
            onEquipoClick = onEquipoClick,
            onPartidoClick = onPartidoClick
        )
    }
}
