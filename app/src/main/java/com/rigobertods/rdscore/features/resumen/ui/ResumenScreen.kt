package com.rigobertods.rdscore.features.resumen.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ResumenScreen(
    onNavigateBack: () -> Unit,
    viewModel: ResumenViewModel = hiltViewModel()
) {
    val precision by viewModel.precision.collectAsStateWithLifecycle()
    val precisionMercado by viewModel.precisionMercado.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.obtenerPrecision()
        viewModel.obtenerPrecisionMercado()
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    PantallaResumen(
        precision = precision,
        precisionMercado = precisionMercado,
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack
    )
}
