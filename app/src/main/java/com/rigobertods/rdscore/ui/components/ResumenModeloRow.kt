package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.features.partidos.data.ModeloResumen
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.core.ui.Rojo
import com.rigobertods.rdscore.core.ui.VerdeFuerte
import java.util.Locale

@Composable
fun ResumenModeloRow(
    partidosTotales: Int,
    mostrarTotal: Boolean,
    modelo: String,
    resumen: ModeloResumen
) {
    Column (
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Text(
            text = modelo,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                if (mostrarTotal) {
                    Text(
                        text = stringResource(R.string.summary_total_matches),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = stringResource(R.string.summary_recommendations),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.summary_hits),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.summary_roi),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column (
                modifier = Modifier.weight(1f).padding(start = 64.dp)
            ){
                if (mostrarTotal) {
                    Text(
                        text = "$partidosTotales",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Text(
                    text = "${resumen.apuestas}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "${resumen.aciertosBrutos}/${resumen.apuestas} (${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            resumen.aciertos
                        )
                    }%)",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "${String.format(Locale.getDefault(), "%.2f", resumen.roi)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (resumen.roi >= 0) VerdeFuerte else Rojo
                )
            }
        }
    }
}
