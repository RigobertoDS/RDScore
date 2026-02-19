package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.features.partidos.data.Resumen
import com.rigobertods.rdscore.features.partidos.data.ResumenMercado
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaginaResumen(
    resumen: Resumen,
    resumenMercado: ResumenMercado
) {
    val models = listOf(
        stringResource(R.string.model_conservative),
        stringResource(R.string.model_moderate),
        stringResource(R.string.model_aggressive)
    )
    val mercados = listOf(
        stringResource(R.string.market_winner),
        stringResource(R.string.market_btts),
        stringResource(R.string.market_over_under)
    )

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { models.size }
    )
    val pagerStateMercado = rememberPagerState(
        initialPage = 0,
        pageCount = { mercados.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Text(
        text = stringResource(R.string.summary_title),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(16.dp)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                models.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> ResumenModeloRow(resumen.partidosTotales, false, stringResource(R.string.summary_model_format, models[0]), resumen.modeloConservador)
                    1 -> ResumenModeloRow(resumen.partidosTotales, false, stringResource(R.string.summary_model_format, models[1]), resumen.modeloModerado)
                    else -> ResumenModeloRow(resumen.partidosTotales, false, stringResource(R.string.summary_model_format, models[2]), resumen.modeloArriesgado)
                }
            }
            PrimaryTabRow(
                selectedTabIndex = pagerStateMercado.currentPage,
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(pagerStateMercado.currentPage),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                mercados.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerStateMercado.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerStateMercado.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerStateMercado,
                modifier = Modifier
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 ->
                        when (pagerState.currentPage) {
                            0 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[0]),
                                    resumenMercado.resultado.conservador
                                )
                            }
                            1 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[0]),
                                    resumenMercado.resultado.moderado
                                )
                            }
                            else -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[0]),
                                    resumenMercado.resultado.agresivo
                                )
                            }
                        }
                    1 ->
                        when (pagerState.currentPage) {
                            0 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[1]),
                                    resumenMercado.btts.conservador
                                )
                            }
                            1 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[1]),
                                    resumenMercado.btts.moderado
                                )
                            }
                            else -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[1]),
                                    resumenMercado.btts.agresivo
                                )
                            }
                        }
                    else ->
                        when (pagerState.currentPage) {
                            0 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[2]),
                                    resumenMercado.over.conservador
                                )
                            }
                            1 -> {
                                ResumenModeloRow(
                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[2]),
                                    resumenMercado.over.moderado
                                )
                            }
                            else -> {

                                ResumenModeloRow(

                                    resumen.partidosTotales,
                                    false,
                    stringResource(R.string.summary_market_format, mercados[2]),
                                    resumenMercado.over.agresivo
                                )
                            }
                        }
                }
            }
        }
    }
}
