package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.features.partidos.data.Partido
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion
import com.rigobertods.rdscore.ui.util.traducirMensajeUi
import com.rigobertods.rdscore.ui.util.traducirPrediccion
import java.util.Locale

@Composable
fun ContenidoRecomendaciones(partido: Partido) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp)
        ) {
        Column {
            // Si el partido ha finalizado, muestra el resultado de forma prominente
            if (partido.estado == "FT") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.final_result), style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${partido.golesLocal} - ${partido.golesVisitante}",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
            // Usamos 'when' para comprobar el tipo de la predicción
            when (val tipoPred = partido.prediccion) {

                // Caso 1: La predicción es detallada (el objeto completo)
                is TipoPrediccion.Detallada -> {
                    // Dentro de este bloque, 'tipoPred.datos' es de tipo 'Prediccion' y puedes acceder a sus campos
                    val datosPrediccion = tipoPred.datos
                    
                    // Verificar si los datos internos son null (puede pasar por deserialización de Room)
                    @Suppress("SENSELESS_COMPARISON")
                    if (datosPrediccion.resultado1x2 == null || datosPrediccion.btts == null || datosPrediccion.over25 == null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = stringResource(R.string.error_loading_predictions),
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                    PrediccionCard(
                        titulo = stringResource(R.string.winner_title),
                        textoPrincipal = traducirPrediccion(datosPrediccion.resultado1x2.prediccion),
                        probabilidadesExtra = listOf(
                            stringResource(R.string.prob_local_format, String.format(Locale.getDefault(), "%.1f", datosPrediccion.resultado1x2.probabilidades.local * 100)),
                            stringResource(R.string.prob_draw_format, String.format(Locale.getDefault(), "%.1f", datosPrediccion.resultado1x2.probabilidades.empate * 100)),
                            stringResource(R.string.prob_away_format, String.format(Locale.getDefault(), "%.1f", datosPrediccion.resultado1x2.probabilidades.visitante * 100))
                        ).joinToString("\n"),
                        // --- Pasa las cuotas de 1X2 ---
                        etiquetasCuotas = listOf("1", "X", "2"),
                        valoresCuotas = listOf(
                            partido.cuotaLocal,
                            partido.cuotaEmpate,
                            partido.cuotaVisitante
                        ),
                        // ---
                        recomendacionConservadora = datosPrediccion.resultado1x2.recomendacion.conservadora,
                        recomendacionModerada = datosPrediccion.resultado1x2.recomendacion.moderada,
                        recomendacionArriesgada = datosPrediccion.resultado1x2.recomendacion.arriesgada,
                        coroutineScope = coroutineScope,
                        scrollState = scrollState
                    )

                    Spacer(Modifier.height(16.dp))

                    // ------------------ CARD BTTS --------------------
                    val probBttsYes = String.format(Locale.getDefault(), "%.1f", datosPrediccion.btts.probabilidad * 100)
                    val probBttsNo = String.format(Locale.getDefault(), "%.1f", (1 - datosPrediccion.btts.probabilidad) * 100)
                    val textoBtts = if (datosPrediccion.btts.prediccion == "Sí") {
                        listOf(
                            stringResource(R.string.prob_btts_yes_format, probBttsYes),
                            stringResource(R.string.prob_btts_no_format, probBttsNo)
                        ).joinToString("\n")
                    } else {
                        listOf(
                            stringResource(R.string.prob_btts_yes_format, probBttsNo),
                            stringResource(R.string.prob_btts_no_format, probBttsYes)
                        ).joinToString("\n")
                    }
                    PrediccionCard(
                        titulo = stringResource(R.string.btts_title),
                        textoPrincipal = traducirPrediccion(datosPrediccion.btts.prediccion),
                        probabilidadesExtra = textoBtts,
                        // --- Pasa las cuotas de BTTS ---
                        etiquetasCuotas = listOf(stringResource(R.string.yes_label), stringResource(R.string.no_label)),
                        valoresCuotas = listOf(partido.cuotaBtts, partido.cuotaBttsNo),
                        // ---
                        recomendacionConservadora = datosPrediccion.btts.recomendacion.conservadora,
                        recomendacionModerada = datosPrediccion.btts.recomendacion.moderada,
                        recomendacionArriesgada = datosPrediccion.btts.recomendacion.arriesgada,
                        coroutineScope = coroutineScope,
                        scrollState = scrollState
                    )

                    Spacer(Modifier.height(16.dp))

                    // ------------------ CARD OVER/UNDER --------------------
                    val probOver = String.format(Locale.getDefault(), "%.1f", datosPrediccion.over25.probabilidad * 100)
                    val probUnder = String.format(Locale.getDefault(), "%.1f", (1 - datosPrediccion.over25.probabilidad) * 100)
                    val textoOver = if (datosPrediccion.over25.prediccion == "Over") {
                        listOf(
                            stringResource(R.string.prob_over_format, probOver),
                            stringResource(R.string.prob_under_format, probUnder)
                        ).joinToString("\n")
                    } else {
                        listOf(
                            stringResource(R.string.prob_over_format, probUnder),
                            stringResource(R.string.prob_under_format, probOver)
                        ).joinToString("\n")
                    }
                    PrediccionCard(
                        titulo = stringResource(R.string.over_under_title),
                        textoPrincipal = traducirPrediccion(datosPrediccion.over25.prediccion),
                        probabilidadesExtra = textoOver,
                        // --- Pasa las cuotas de Over/Under ---
                        etiquetasCuotas = listOf(stringResource(R.string.more_label), stringResource(R.string.less_label)),
                        valoresCuotas = listOf(partido.cuotaOver, partido.cuotaUnder),
                        // ---
                        recomendacionConservadora = datosPrediccion.over25.recomendacion.conservadora,
                        recomendacionModerada = datosPrediccion.over25.recomendacion.moderada,
                        recomendacionArriesgada = datosPrediccion.over25.recomendacion.arriesgada,
                        coroutineScope = coroutineScope,
                        scrollState = scrollState
                    )
                    } // End of else block for null check
                }

                // Caso 2: La predicción es un simple String
                is TipoPrediccion.Simple -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = traducirMensajeUi(tipoPred.mensaje),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Caso 3: La predicción no está disponible
                is TipoPrediccion.NoDisponible -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = stringResource(R.string.prediction_not_available),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
