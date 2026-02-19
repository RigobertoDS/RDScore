package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.StatItem
import com.rigobertods.rdscore.core.common.mapEquiposToStatItems

@Composable
fun ContenidoEstadisticas(partido: Partido) {
    val statsList = mapEquiposToStatItems(partido.estadisticasLocal, partido.estadisticasVisitante)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp) // Espacio arriba y abajo
    ) {
        items(statsList) { statItem ->
            StatRow(item = statItem) // Un composable que pinta una fila
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun StatRow(item: StatItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.valorLocal, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, style = MaterialTheme.typography.bodyLarge)
        Text(text = stringResource(item.labelResId), modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text(text = item.valorVisitante, modifier = Modifier.weight(1f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyLarge)
    }
}
