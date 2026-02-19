package com.rigobertods.rdscore.features.equipo.ui

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.rigobertods.rdscore.features.partidos.data.DatosEquipo
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.ui.components.ContenidoEquipoClasificacion
import com.rigobertods.rdscore.ui.components.ContenidoEquipoEstadisticas
import com.rigobertods.rdscore.ui.components.ContenidoEquipoPartidos
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleEquipo(
    idEquipo: Int,
    idLiga: Int,
    datosEquipo: DatosEquipo,
    isLoading: Boolean,
    selectedTabIndex: Int = 1,
    onTabChanged: (Int) -> Unit = {},
    selectedLeagueId: Int = idLiga,
    onLeagueChanged: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onEquipoClick: (idEquipo: Int, idLiga: Int) -> Unit = { _, _ -> },
    onPartidoClick: (Int) -> Unit = {}
) {
    Scaffold (
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                ContenidoDetalleEquipo(
                    datosEquipo = datosEquipo,
                    idEquipo = idEquipo,
                    selectedLeagueId = selectedLeagueId,
                    onLeagueChanged = onLeagueChanged,
                    selectedTabIndex = selectedTabIndex,
                    onTabChanged = onTabChanged,
                    onEquipoClick = onEquipoClick,
                    onPartidoClick = onPartidoClick
                )
            }
        }
    }
}

@Composable
fun ContenidoDetalleEquipo(
    datosEquipo: DatosEquipo,
    idEquipo: Int,
    selectedLeagueId: Int,
    onLeagueChanged: (Int) -> Unit,
    selectedTabIndex: Int,
    onTabChanged: (Int) -> Unit,
    onEquipoClick: (Int, Int) -> Unit,
    onPartidoClick: (Int) -> Unit
) {
    val tabTitles = listOf(
        stringResource(R.string.tab_statistics),
        stringResource(R.string.tab_standings),
        stringResource(R.string.tab_matches)
    )
    // Tab and league state now comes from ViewModel via parameters
    val pagerState = rememberPagerState(initialPage = selectedTabIndex) { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()
    
    // Ensure selected league is valid - fallback to first available if not found
    val ligasDisponibles = datosEquipo.equiposLigaEquipo.map { it.idLiga }
    val idLigaSeleccionada = if (selectedLeagueId in ligasDisponibles) {
        selectedLeagueId
    } else {
        ligasDisponibles.firstOrNull() ?: selectedLeagueId
    }
    
    // Sync pager state with ViewModel when user changes tab
    LaunchedEffect(pagerState.currentPage) {
        if (selectedTabIndex != pagerState.currentPage) {
            onTabChanged(pagerState.currentPage)
        }
    }
    
    // Restore pager position when returning from navigation
    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            pagerState.scrollToPage(selectedTabIndex)
        }
    }

    val equipo = datosEquipo.equiposLigaEquipo.find { it.idLiga == idLigaSeleccionada }?.equipo ?: Equipo()
    val equipos = datosEquipo.equiposLigaEquipo.find { it.idLiga == idLigaSeleccionada }?.equiposLiga ?: emptyList()
    val partidos = datosEquipo.partidosEquipo.find { it.idLiga == idLigaSeleccionada }?.partidosLiga ?: emptyList()

    val proximoPartido = partidos.filter { it.resultado == -1 }.minByOrNull { p ->
        val pts = p.fecha.split("/")
        if (pts.size == 3) "${pts[2]}${pts[1]}${pts[0]}" else ""
    }

    val nombreRival = when {
        proximoPartido == null -> "No hay partidos"
        proximoPartido.estadisticasLocal.id == idEquipo -> proximoPartido.nombreEquipoVisitante
        else -> proximoPartido.nombreEquipoLocal
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // ------------------ PARTE DE ARRIBA FIJA --------------------
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.align(Alignment.Start)
        ) {
            AsyncImage(
                model = equipo.logo,
                contentDescription = equipo.nombre,
                modifier = Modifier.size(90.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = equipo.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.form_label),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(4.dp))
                    IndicadorForma(forma = equipo.ultimosPartidos)
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.next_match_label),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = nombreRival,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        // ------------------ SPINNER DE SELECCION DE LIGA ------------------
        SelectorLiga(
            idLigaSeleccionada = idLigaSeleccionada,
            datosEquipo = datosEquipo,
            onLigaSelected = { nuevoId -> onLeagueChanged(nuevoId) }
        )
        // ------------------ PESTAÑAS DE INFORMACIÓN ------------------
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
        Spacer(Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> ContenidoEquipoEstadisticas(equipo)
                1 -> ContenidoEquipoClasificacion(equipo, equipos, onEquipoClick)
                2 -> ContenidoEquipoPartidos(equipo, partidos, onPartidoClick)
            }
        }
    }
}

@Composable
fun IndicadorForma(forma: String) {
    // Labels translated based on current locale
    val labelWin = stringResource(R.string.form_win)
    val labelDraw = stringResource(R.string.form_draw)
    val labelLoss = stringResource(R.string.form_loss)
    
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        forma.forEach { letra ->
            // Use original characters from API: W=Win, D=Draw, L=Loss
            val (colorFondo, texto) = when (letra) {
                'W' -> Color(0xFF4CAF50) to labelWin
                'D' -> Color(0xFF9E9E9E) to labelDraw
                'L' -> Color(0xFFF44336) to labelLoss
                else -> Color.DarkGray to letra.toString()
            }
            Surface(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(24.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = colorFondo
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = texto,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// transformarForma is no longer needed since we use original W/D/L from API

data class InfoLiga(val nombre: String, val logo: String)

fun buscarLiga (idLiga: Int, datosEquipo: DatosEquipo): InfoLiga {
    val ligaEncontrada = datosEquipo.equiposLigaEquipo.find { it.idLiga == idLiga }
    return InfoLiga(
        nombre = ligaEncontrada?.equipo?.liga?.nombre ?: "Liga desconocida",
        logo = ligaEncontrada?.equipo?.liga?.logo ?: ""
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorLiga(
    idLigaSeleccionada: Int,
    datosEquipo: DatosEquipo,
    onLigaSelected: (Int) -> Unit
) {
    // 1. Buscamos la info de la liga que está seleccionada actualmente para el "botón"
    val ligaActual = buscarLiga(idLigaSeleccionada, datosEquipo)
    var expanded by remember { mutableStateOf(false) }

    // Contenedor principal del Dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Campo de texto que actúa como "Spinner"
        OutlinedTextField(
            value = ligaActual.nombre,
            onValueChange = {},
            readOnly = true, // Evita que el usuario escriba
            label = { Text(stringResource(R.string.label_competition)) },
            leadingIcon = {
                AsyncImage(
                    model = ligaActual.logo,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )

        // Lista de opciones que se despliega
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            datosEquipo.equiposLigaEquipo.forEach { competicion ->
                val info = buscarLiga(competicion.idLiga, datosEquipo)
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = info.logo,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = info.nombre)
                        }
                    },
                    onClick = {
                        onLigaSelected(competicion.idLiga)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
