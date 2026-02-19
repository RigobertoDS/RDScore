package com.rigobertods.rdscore.features.partidos.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.core.common.LanguageInfo
import com.rigobertods.rdscore.core.common.UiState

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToDetalle: (Partido) -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToResumen: () -> Unit,
    onNavigateToCuotas: () -> Unit,
    currentLanguage: String = "es",
    supportedLanguages: List<LanguageInfo> = emptyList(),
    onLanguageChange: (String) -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val leagues by viewModel.leagues.collectAsStateWithLifecycle()
    
    val partidosMap = uiState.data ?: emptyMap()
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    PantallaMain(
        snackbarHostState = snackbarHostState,
        partidosMap = partidosMap,
        leagues = leagues,
        isLoadingGlobal = isLoading,
        onResumenClick = onNavigateToResumen,
        onCuotasCalientesClick = onNavigateToCuotas,
        onPerfilClick = onNavigateToPerfil,
        onPartidoClick = onNavigateToDetalle,
        onFetchMatches = { fecha ->
            viewModel.obtenerPartidosDeLaFecha(fecha)
        },
        onToggleTheme = onToggleTheme,
        isDarkTheme = isDarkTheme,
        currentLanguage = currentLanguage,
        supportedLanguages = supportedLanguages,
        onLanguageChange = onLanguageChange
    )
}
