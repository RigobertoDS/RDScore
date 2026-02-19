package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.rigobertods.rdscore.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.core.ui.VerdeFuerte

@Composable
fun ContenidoEquipoClasificacion(
    equipoBuscado: Equipo,
    equipos: List<Equipo>,
    onEquipoClick: (idEquipo: Int, idLiga: Int) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TablaHeader()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(equipos) { equipo ->
                FilaEquipo(
                    idEquipoBuscado = equipoBuscado.id,
                    equipo = equipo,
                    onClick = { onEquipoClick(equipo.id, equipo.liga.id) }
                )
            }
        }
    }
}

@Composable
fun TablaHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1.5f))
        Text(
            text = stringResource(R.string.header_team),
            modifier = Modifier.weight(4f),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        val headerStyle = MaterialTheme.typography.labelSmall
        val headerWeight = 1f

        Text(stringResource(R.string.header_pj), Modifier.weight(headerWeight), style = headerStyle, textAlign = TextAlign.Center)
        Text(stringResource(R.string.header_g), Modifier.weight(headerWeight), style = headerStyle, textAlign = TextAlign.Center)
        Text(stringResource(R.string.header_e), Modifier.weight(headerWeight), style = headerStyle, textAlign = TextAlign.Center)
        Text(stringResource(R.string.header_p), Modifier.weight(headerWeight), style = headerStyle, textAlign = TextAlign.Center)
        Text(stringResource(R.string.header_pt), Modifier.weight(headerWeight), style = headerStyle, textAlign = TextAlign.Center)
    }
}

@Composable
fun FilaEquipo(
    idEquipoBuscado: Int,
    equipo: Equipo,
    onClick: () -> Unit = {}
) {
    val esSeleccionado = equipo.id == idEquipoBuscado

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (esSeleccionado) VerdeFuerte.copy(alpha = 0.2f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${equipo.posicion}.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.width(4.dp))
            AsyncImage(
                model = equipo.logo,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = equipo.nombre,
            modifier = Modifier.weight(4f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
        )
        val statModifier = Modifier.weight(1f)
        val statStyle = MaterialTheme.typography.bodySmall
        val align = TextAlign.Center

        Text("${equipo.partidosTotales}", statModifier, style = statStyle, textAlign = align)
        Text("${equipo.victoriasTotales}", statModifier, style = statStyle, textAlign = align)
        Text("${equipo.empatesTotales}", statModifier, style = statStyle, textAlign = align)
        Text("${equipo.derrotasTotales}", statModifier, style = statStyle, textAlign = align)
        Text(
            text = "${equipo.puntos}",
            modifier = statModifier,
            style = statStyle,
            textAlign = align,
            fontWeight = FontWeight.Bold
        )
    }
}
