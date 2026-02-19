package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.Partido

@Composable
fun ContenidoEquipoPartidos (equipoBuscado: Equipo, partidos: List<Partido>, onPartidoClick: (Int) -> Unit = {}) {
    val partidosOrdenados = remember(partidos) {
        partidos.sortedByDescending { p ->
            val pts = p.fecha.split("/")
            if (pts.size == 3) "${pts[2]}${pts[1]}${pts[0]}" else ""
        }
    }
    val listState = rememberLazyListState()
    val indiceProximo = remember(partidosOrdenados) {
        val index = partidosOrdenados.indexOfLast { it.resultado == -1 }
        if (index != -1) index else 0
    }
    LaunchedEffect(partidosOrdenados) {
        if (partidosOrdenados.isNotEmpty()) {
            listState.animateScrollToItem(indiceProximo)
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(partidosOrdenados) { partido ->
            FilaPartido(
                idEquipo = equipoBuscado.id,
                partido = partido,
                onClick = { onPartidoClick(partido.idPartido) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun FilaPartido (idEquipo: Int, partido: Partido, onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ){
        Column (
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "${partido.fecha}, ${partido.hora}")
            Spacer(modifier = Modifier.size(8.dp))
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = partido.estadisticasLocal.logo,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = partido.nombreEquipoLocal, fontWeight = if (partido.estadisticasLocal.id == idEquipo) FontWeight.Bold else FontWeight.Normal)
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = partido.estadisticasVisitante.logo,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = partido.nombreEquipoVisitante, fontWeight = if (partido.estadisticasVisitante.id == idEquipo) FontWeight.Bold else FontWeight.Normal)
                    }
                }
                Column {
                    Row {
                        Column {
                            Text(text = if (partido.estado == "FT") "${partido.golesLocal}" else "-")
                            Text(text = if (partido.estado == "FT") "${partido.golesVisitante}" else "-")
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            DefinirGanador(idEquipo, partido)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefinirGanador (idEquipo: Int, partido: Partido) {
    // Get translated labels
    val labelWin = stringResource(R.string.form_win)
    val labelDraw = stringResource(R.string.form_draw)
    val labelLoss = stringResource(R.string.form_loss)
    
    var esLocal = false
    if (partido.estadisticasLocal.id == idEquipo) {
        esLocal = true
    } else if (partido.estadisticasVisitante.id == idEquipo) {
        esLocal = false
    }
    val resultado = partido.resultado
    val (letra, colorFondo) = when (resultado) {
        1 if esLocal -> {
            labelWin to Color(0xFF4CAF50)
        }
        2 if !esLocal -> {
            labelWin to Color(0xFF4CAF50)
        }
        0 -> {
            labelDraw to Color(0xFF9E9E9E)
        }
        -1 -> {
            "" to Color.Transparent
        }
        else -> {
            labelLoss to Color(0xFFF44336)
        }
    }
    Surface(
        modifier = Modifier
            .padding(end = 4.dp)
            .size(24.dp),
        shape = CircleShape,
        color = colorFondo
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = letra,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

