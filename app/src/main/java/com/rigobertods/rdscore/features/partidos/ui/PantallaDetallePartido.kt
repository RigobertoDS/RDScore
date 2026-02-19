package com.rigobertods.rdscore.features.partidos.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.ui.components.ContenidoEstadisticas
import com.rigobertods.rdscore.ui.components.ContenidoInformacion
import com.rigobertods.rdscore.ui.components.ContenidoRecomendaciones
import com.rigobertods.rdscore.ui.components.EquipoLogoYNombre
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetallePartido(
    partido: Partido,
    onIconClick: (Equipo) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val tabTitles = listOf(
        stringResource(R.string.tab_predictions),
        stringResource(R.string.tab_statistics),
        stringResource(R.string.tab_info)
    )
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    val logoLocal = partido.estadisticasLocal.logo
    val logoVisitante = partido.estadisticasVisitante.logo

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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // ------------------ TÍTULO DE LA LIGA --------------------
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    AsyncImage(
                        model = partido.estadisticasLocal.liga.logo,
                        contentDescription = partido.estadisticasLocal.liga.nombre,
                        modifier = Modifier
                            .size(45.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = partido.estadisticasLocal.liga.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                // ------------------ FECHA Y HORA -------------------------
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${partido.fecha} - ${partido.hora}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(Modifier.height(16.dp))
                // ------------------ ESCUDOS + NOMBRES --------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EquipoLogoYNombre(
                        url = logoLocal,
                        nombre = partido.nombreEquipoLocal,
                        onIconClick = {onIconClick(partido.estadisticasLocal)}
                    )
                    Text(
                        "VS",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    EquipoLogoYNombre(
                        url = logoVisitante,
                        nombre = partido.nombreEquipoVisitante,
                        onIconClick = {onIconClick(partido.estadisticasVisitante)}
                    )
                }
                Spacer(Modifier.height(20.dp))
                // ------------------ PESTAÑAS DE INFORMACIÓN --------------------
                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
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
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> ContenidoRecomendaciones(partido = partido)
                        1 -> ContenidoEstadisticas(partido = partido)
                        2 -> ContenidoInformacion(partido = partido)
                    }
                }
            }
        }
    }
}
