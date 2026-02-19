package com.rigobertods.rdscore.features.partidos.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.core.common.LanguageInfo
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.ui.components.InfoDialog
import com.rigobertods.rdscore.ui.components.LanguageSelectorDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

import com.rigobertods.rdscore.features.ligas.data.Liga

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMain(
    snackbarHostState: SnackbarHostState,
    partidosMap: Map<LocalDate, List<Partido>>,
    leagues: Map<Int, Liga>,
    isLoadingGlobal: Boolean,
    onResumenClick: () -> Unit,
    onCuotasCalientesClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onPartidoClick: (Partido) -> Unit,
    onFetchMatches: (LocalDate) -> Unit,
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean,
    currentLanguage: String = "es",
    supportedLanguages: List<LanguageInfo> = emptyList(),
    onLanguageChange: (String) -> Unit = {}
) {
    val showInfo = remember { mutableStateOf(false) }
    val showLanguageSelector = remember { mutableStateOf(false) }

    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = stringResource(R.string.cd_logo),
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                },
                actions = {
                    IconButton(onClick = { showLanguageSelector.value = true }) {
                        Text(
                            text = when (currentLanguage) {
                                "es" -> "🇪🇸"
                                "en" -> "🇬🇧"
                                "de" -> "🇩🇪"
                                "fr" -> "🇫🇷"
                                "it" -> "🇮🇹"
                                else -> "🌍"
                            },
                            fontSize = 24.sp
                        )
                    }
                    IconButton(onClick = { showInfo.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(R.string.cd_info),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = stringResource(R.string.cd_change_theme),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onPerfilClick) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = stringResource(R.string.cd_profile),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PantallaPrincipalPartidos(
                partidosMap = partidosMap,
                leagues = leagues,
                isLoadingGlobal = isLoadingGlobal,
                onResumenClick = onResumenClick,
                onCuotasCalientesClick = onCuotasCalientesClick,
                onFetchMatches = onFetchMatches,
                onPartidoClick = onPartidoClick
            )
        }
        if (showInfo.value) {
            InfoDialog (
                show = showInfo.value,
                onDismiss = { showInfo.value = false }
            )
        }
        if (showLanguageSelector.value && supportedLanguages.isNotEmpty()) {
            LanguageSelectorDialog(
                currentLanguage = currentLanguage,
                languages = supportedLanguages,
                onLanguageSelected = { newLanguage ->
                    // First dismiss the dialog
                    showLanguageSelector.value = false
                    // Wait for dismissal animation to finish before recreating activity
                    // This prevents keyboard focus issues
                    coroutineScope.launch {
                        delay(300)
                        onLanguageChange(newLanguage)
                    }
                },
                onDismiss = { showLanguageSelector.value = false }
            )
        }
    }
}
