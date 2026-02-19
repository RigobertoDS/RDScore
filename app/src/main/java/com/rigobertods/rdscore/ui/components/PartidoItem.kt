package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import coil.compose.AsyncImage
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion
import com.rigobertods.rdscore.features.partidos.util.PredictionHelper
import com.rigobertods.rdscore.ui.util.DateUtils
import com.rigobertods.rdscore.ui.util.traducirPrediccion
import java.util.Locale

@Composable
fun PartidoItem(
    partido: Partido,
    pagina: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
     {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Columna Izquierda: Equipos ---
            Column(modifier = Modifier.weight(1f)) {
                ContenidoFechaHora(partido)
                // Equipo Local
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = partido.estadisticasLocal.logo,
                        contentDescription = stringResource(R.string.cd_team_logo, partido.nombreEquipoLocal),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(partido.nombreEquipoLocal, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Equipo Visitante
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = partido.estadisticasVisitante.logo,
                        contentDescription = stringResource(R.string.cd_team_logo, partido.nombreEquipoVisitante),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(partido.nombreEquipoVisitante, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            // --- Columna Derecha: Contenido Dinámico (Unificado V2) ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Si el partido ha terminado, mostramos el resultado arriba
                if (partido.estado == "FT") {
                    ContenidoResultadoFinal(partido)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Si hay predicción detallada, la mostramos SIEMPRE (futuro o pasado)
                if (partido.prediccion is TipoPrediccion.Detallada) {
                    ContenidoPrediccion(partido, partido.prediccion, pagina)
                } else if (partido.estado != "FT") {
                    // Si no es FT y no hay predicción, mostramos el mensaje de "No hay predicción"
                    Text(stringResource(R.string.no_predictions), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ContenidoResultadoFinal(partido: Partido) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.finished), style = MaterialTheme.typography.labelSmall, color = Color.Red)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${partido.golesLocal} - ${partido.golesVisitante}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ContenidoPrediccion(partido: Partido, prediccion: TipoPrediccion.Detallada, pagina: Int) {
    val acierto = PredictionHelper.verificarAcierto(partido, pagina)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.prediction), style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            val textoPred = when (pagina) {
                0 -> traducirPrediccion(prediccion.datos.resultado1x2.prediccion)
                1 -> traducirPrediccion(prediccion.datos.btts.prediccion)
                2 -> traducirPrediccion(prediccion.datos.over25.prediccion)
                else -> ""
            }

            val porcentaje = when (pagina) {
                0 -> prediccion.datos.resultado1x2.probabilidadMax
                1 -> prediccion.datos.btts.probabilidad
                2 -> prediccion.datos.over25.probabilidad
                else -> 0.0
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = textoPred,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Icono de acierto/fallo si el partido terminó y FUE RECOMENDADO
                    if (acierto != null && PredictionHelper.esRecomendada(partido, pagina)) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (acierto) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (acierto) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
                Text(
                    text = String.format(Locale.getDefault(), "%.0f%%", porcentaje * 100),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun ContenidoFechaHora(partido: Partido) {
        Text(
            text = "${DateUtils.formatearFechaParaDisplay(partido.fecha)}, ${partido.hora}",
            style = MaterialTheme.typography.labelSmall
        )
    Spacer(modifier = Modifier.height(8.dp))
}
