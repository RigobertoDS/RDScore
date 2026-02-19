package com.rigobertods.rdscore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PrediccionCard(
    titulo: String,
    textoPrincipal: String,
    probabilidadesExtra: String? = null,
    etiquetasCuotas: List<String> = emptyList(),
    valoresCuotas: List<Double> = emptyList(),
    recomendacionModerada: Int,
    recomendacionConservadora: Int,
    recomendacionArriesgada: Int,
    coroutineScope: CoroutineScope,
    scrollState: ScrollState
) {
    var isExpanded by remember { mutableStateOf(false) }
    var cardYPosition by remember { mutableFloatStateOf(0f) }
    
    // Determinar si hay alguna recomendación activa
    val tieneRecomendacion = recomendacionConservadora == 1 || 
                             recomendacionModerada == 1 || 
                             recomendacionArriesgada == 1
    
    // Color del borde según si hay recomendación
    val borderColor = if (tieneRecomendacion) {
        androidx.compose.ui.graphics.Color(0xFF4CAF50) // Verde para recomendación
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (tieneRecomendacion) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = MaterialTheme.shapes.medium
                    )
                } else {
                    Modifier
                }
            )
            .onGloballyPositioned { coordinates ->
                cardYPosition = coordinates.positionInParent().y
            }
            .clickable {
                isExpanded = !isExpanded
                if (isExpanded) {
                    coroutineScope.launch {
                        // Esperamos un poco para que la UI se recomponga tras la expansión
                        kotlinx.coroutines.delay(100)
                        // Hacemos scroll a la posición Y de la tarjeta
                        scrollState.animateScrollTo(cardYPosition.toInt())
                    }
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(16.dp)) {
            // --- PARTE SUPERIOR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        titulo,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = textoPrincipal,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    if (probabilidadesExtra != null || valoresCuotas.any { it > 0 }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween // Clave para la alineación
                        ) {
                            // --- COLUMNA DE PROBABILIDADES (Izquierda) ---
                            if (probabilidadesExtra != null) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(stringResource(R.string.probabilities), style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(4.dp))
                                    // El texto con el desglose que pasamos como parámetro
                                    Text(probabilidadesExtra, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                                }
                            }

                            if (valoresCuotas.any { it > 0 }) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(stringResource(R.string.odds_label), style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(4.dp))
                                    // Genera dinámicamente las filas de cuotas
                                    valoresCuotas.forEachIndexed { index, cuota ->
                                        if (index < etiquetasCuotas.size) {
                                            val etiqueta = etiquetasCuotas[index]
                                            val textoCuota: String
                                            if (cuota > 0) {
                                                // 1. Calcula la probabilidad implícita como un número
                                                val probImplicita = (1 / cuota) * 100

                                                // 2. Formatea la cuota y la probabilidad por separado
                                                val cuotaFormateada = String.format(Locale.getDefault(), "%.2f", cuota)
                                                val probFormateada = String.format(Locale.getDefault(), "%.1f", probImplicita)

                                                textoCuota = "$cuotaFormateada ($probFormateada%)"
                                            } else {
                                                textoCuota = stringResource(R.string.not_available)
                                            }
                                            Text(
                                                text = "$etiqueta: $textoCuota",
                                                style = MaterialTheme.typography.bodyMedium,
                                                lineHeight = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.recommendations), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    RecomendacionRow(stringResource(R.string.conservative), recomendacionConservadora)
                    RecomendacionRow(stringResource(R.string.moderate), recomendacionModerada)
                    RecomendacionRow(stringResource(R.string.aggressive), recomendacionArriesgada)
                }
            }
        }
    }
}
