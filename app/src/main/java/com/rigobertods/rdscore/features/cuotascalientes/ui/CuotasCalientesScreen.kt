package com.rigobertods.rdscore.features.cuotascalientes.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.features.partidos.data.Partido

@Composable
fun CuotasCalientesScreen(
    onNavigateToDetalle: (Partido) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CuotasCalientesViewModel = hiltViewModel()
) {
    val partidos by viewModel.partidos.collectAsStateWithLifecycle()
    val cuotasCalientes by viewModel.cuotasCalientes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.obtenerCuotasCalientes()
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    PantallaCuotasCalientes(
        partidos = partidos,
        cuotasCalientes = cuotasCalientes,
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onPartidoClick = onNavigateToDetalle
    )
}
