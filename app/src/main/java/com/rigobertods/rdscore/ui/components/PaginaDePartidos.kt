package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.ligas.data.Liga

@Composable
fun PaginaDePartidos(
    filtrosActivos: FiltroState,
    fecha: LocalDate,
    partidos: List<Partido>,
    leagues: Map<Int, Liga>,
    isMatchesLoaded: Boolean,
    onFetchMatches: (LocalDate) -> Unit,
    onPartidoClick: (Partido) -> Unit,
    ligasExpandidas: Set<Int>,
    onLigaToggle: (Int) -> Unit,
    tabSeleccionada: Int,
    onTabSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    // var tabSeleccionada by remember { mutableIntStateOf(0) } // Hoisted to parent
    val titulosTabs = listOf(
        stringResource(R.string.market_winner),
        stringResource(R.string.market_btts),
        stringResource(R.string.market_over_under)
    )

    // LaunchedEffect para solicitar datos si no están cargados.
    LaunchedEffect(fecha) {
        if (!isMatchesLoaded) {
            onFetchMatches(fecha)
        }
    }

    if (!isMatchesLoaded) {
        Box(modifier = Modifier.fillMaxSize()) {}
    } else {
        val partidosFiltrados = if (!filtrosActivos.algunFiltroActivo) {
            partidos
        } else {
            partidos.filter { partido ->
                (partido.prediccion as? TipoPrediccion.Detallada)?.let { det ->
                    val pasaFiltroResultadoConservador = filtrosActivos.resultadoConservador && det.datos.resultado1x2.recomendacion.conservadora == 1
                    val pasaFiltroResultadoModerado = filtrosActivos.resultadoModerado && det.datos.resultado1x2.recomendacion.moderada == 1
                    val pasaFiltroResultadoAgresivo = filtrosActivos.resultadoAgresivo && det.datos.resultado1x2.recomendacion.arriesgada == 1
                    val pasaFiltroBttsConservador = filtrosActivos.bttsConservador && det.datos.btts.recomendacion.conservadora == 1
                    val pasaFiltroBttsModerado = filtrosActivos.bttsModerado && det.datos.btts.recomendacion.moderada == 1
                    val pasaFiltroBttsAgresivo = filtrosActivos.bttsAgresivo && det.datos.btts.recomendacion.arriesgada == 1
                    val pasaFiltroOverUnderConservador = filtrosActivos.overUnderConservador && det.datos.over25.recomendacion.conservadora == 1
                    val pasaFiltroOverUnderModerado = filtrosActivos.overUnderModerado && det.datos.over25.recomendacion.moderada == 1
                    val pasaFiltroOverUnderAgresivo = filtrosActivos.overUnderAgresivo && det.datos.over25.recomendacion.arriesgada == 1
                    pasaFiltroResultadoConservador || pasaFiltroResultadoModerado || pasaFiltroResultadoAgresivo ||
                            pasaFiltroBttsConservador || pasaFiltroBttsModerado || pasaFiltroBttsAgresivo ||
                            pasaFiltroOverUnderConservador || pasaFiltroOverUnderModerado || pasaFiltroOverUnderAgresivo
                } ?: false
            }
        }

        val partidosAgrupados = partidosFiltrados.groupBy { it.idLiga }
        
        // Orden de ligas por importancia (las más importantes primero)
        val ordenLigas = listOf(
            2,   // Champions League
            3,   // Europa League
            848, // Conference League
            140, // La Liga
            141, // La Liga 2
            39,  // Premier League
            40,  // Championship
            78,  // Bundesliga
            79,  // 2. Bundesliga
            135, // Serie A
            136, // Serie B
            61,  // Ligue 1
            62,  // Ligue 2
            88,  // Eredivisie
            94,  // Liga Portugal
            203, // Süper Lig
            71,  // Serie A Brasil
            128, // Liga Profesional Argentina
            253, // Major League Soccer
            262  // Liga MX
        )
        
        // Ordenar las claves de las ligas según la prioridad.
        // Ligas que no estén en la lista aparecerán al final (Int.MAX_VALUE).
        val leagueKeys = partidosAgrupados.keys.sortedBy { idLiga ->
            val index = ordenLigas.indexOf(idLiga)
            if (index != -1) index else Int.MAX_VALUE
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PrimaryTabRow (
                selectedTabIndex = tabSeleccionada,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabSeleccionada),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                titulosTabs.forEachIndexed { index, titulo ->
                    Tab(
                        selected = tabSeleccionada == index,
                        onClick = { onTabSelected(index) },
                        text = { Text(titulo, fontSize = 12.sp) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                if (partidosAgrupados.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_matches_selected_date), modifier = Modifier.padding(16.dp))
                        }
                    }
                } else {
                    leagueKeys.forEach { idLiga ->
                        val partidosDeLaLiga = partidosAgrupados[idLiga]!!
                        val ligaInfo = leagues[idLiga]

                        item(key = "header-$idLiga") {
                            CabeceraLiga(
                                partido = partidosDeLaLiga.first(),
                                ligaInfo = ligaInfo,
                                pagina = tabSeleccionada,
                                estaExpandida = ligasExpandidas.contains(idLiga),
                                onClick = {
                                    val estabaExpandida = idLiga in ligasExpandidas
                                    val esExpandiendo = !estabaExpandida

                                    onLigaToggle(idLiga)

                                    if (esExpandiendo) {
                                        coroutineScope.launch {
                                            val index = leagueKeys.indexOf(idLiga)
                                            if (index != -1) {
                                                listState.animateScrollToItem(index)
                                            }
                                        }
                                    }
                                },
                                partidosDeLaLiga = partidosDeLaLiga,
                                onPartidoClick = onPartidoClick
                            )
                        }
                    }
                }
            }
        }
    }
}

