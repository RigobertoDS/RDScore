package com.rigobertods.rdscore.features.partidos.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.ui.components.BarraNavegacionFechas
import com.rigobertods.rdscore.ui.components.DialogoFiltros
import com.rigobertods.rdscore.ui.components.FiltroState
import com.rigobertods.rdscore.ui.components.PaginaDePartidos
import com.rigobertods.rdscore.core.ui.Naranja
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R

import com.rigobertods.rdscore.features.ligas.data.Liga

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PantallaPrincipalPartidos(
    partidosMap: Map<LocalDate, List<Partido>>,
    leagues: Map<Int, Liga>,
    isLoadingGlobal: Boolean,
    onResumenClick: () -> Unit,
    onCuotasCalientesClick: () -> Unit,
    onFetchMatches: (fecha: LocalDate) -> Unit,
    onPartidoClick: (Partido) -> Unit
) {
    val initialPage = 5000
    val pageCount = 10000

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { pageCount }
    )

    val fechaBase = remember { LocalDate.now() }

    // Fecha basada en la página actual
    val fechaSeleccionada by remember {
        derivedStateOf {
            fechaBase.plusDays((pagerState.currentPage - initialPage).toLong())
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Estado global de carga: true = bloquea toda la pantalla
    // Ya no usamos estaCargandoGlobal interno, usamos el parámetro isLoadingGlobal
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var filtros by rememberSaveable { mutableStateOf(FiltroState()) }
    var mostrarDialogoFiltros by remember { mutableStateOf(false) }
    var ligasExpandidas by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }
    var tabSeleccionadaGlobal by rememberSaveable { mutableIntStateOf(0) }

    // Overlay de carga que cubre toda la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column {
                    /*
                    AQUI PUEDO AGREGAR UN BANNER DE NOTICIAS
                    BannerAviso(
                        titulo = stringResource(R.string.banner_result_disabled_title),
                        texto = stringResource(R.string.banner_result_disabled_text)
                    )*/
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { onResumenClick() }
                                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.QueryStats,
                                contentDescription = stringResource(R.string.cd_summary_icon),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                stringResource(R.string.view_model_stats),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = stringResource(R.string.nav_summary)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 4.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { onCuotasCalientesClick() }
                                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = stringResource(R.string.cd_hot_odds_icon),
                                tint = Naranja
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                stringResource(R.string.hot_odds_button),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = stringResource(R.string.hot_odds)
                            )
                        }
                    }

                    // 3. La BarraNavegacionFechas va después, dentro de la misma Column.
                    BarraNavegacionFechas(
                        fecha = fechaSeleccionada,
                        enabled = !isLoadingGlobal || pagerState.isScrollInProgress,
                        onFechaAnterior = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        onFechaSiguiente = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        onCalendarioClick = { if (!isLoadingGlobal) mostrarDatePicker = true },
                        onFiltroClick = { if (!isLoadingGlobal) mostrarDialogoFiltros = true }
                    )
                }
            }
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                userScrollEnabled = !isLoadingGlobal || pagerState.isScrollInProgress
            ) { page ->
                val fechaPagina = fechaBase.plusDays((page - initialPage).toLong())
                
                PaginaDePartidos(
                    filtrosActivos = filtros,
                    fecha = fechaPagina,
                    partidos = partidosMap[fechaPagina] ?: emptyList(),
                    leagues = leagues,
                    isMatchesLoaded = partidosMap.containsKey(fechaPagina),
                    onFetchMatches = onFetchMatches,
                    onPartidoClick = onPartidoClick,
                    ligasExpandidas = ligasExpandidas,
                    onLigaToggle = { idLiga ->
                        ligasExpandidas = if (ligasExpandidas.contains(idLiga)) {
                            ligasExpandidas - idLiga
                        } else {
                            ligasExpandidas + idLiga
                        }
                    },
                    tabSeleccionada = tabSeleccionadaGlobal,
                    onTabSelected = { tabSeleccionadaGlobal = it }
                )
            }
        }

        // OVERLAY DE CARGA GLOBAL: Solo se muestra si está cargando Y el swipe ha terminado.

        if (isLoadingGlobal && !pagerState.isScrollInProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                    .clickable(enabled = false) {} // bloquea cualquier toque
                ,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 5.dp
                )
            }
        }

        // DatePicker y diálogo de filtros
        if (mostrarDatePicker) {
            val zoneId = ZoneId.systemDefault()
            val initialMillis = fechaSeleccionada.atTime(12, 0).atZone(zoneId).toInstant().toEpochMilli()
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val nuevaFecha = Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
                            val diasDiff = ChronoUnit.DAYS.between(fechaBase, nuevaFecha)
                            val paginaObjetivo = (initialPage + diasDiff).toInt()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(paginaObjetivo)
                            }
                        }
                    }) { Text(stringResource(R.string.ok), color = MaterialTheme.colorScheme.onBackground) }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) { Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onBackground) }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (mostrarDialogoFiltros) {
            DialogoFiltros(
                filtroState = filtros,
                onDismiss = { mostrarDialogoFiltros = false },
                onAplicarFiltros = { nuevosFiltros ->
                    filtros = nuevosFiltros
                    mostrarDialogoFiltros = false
                }
            )
        }
    }
}
