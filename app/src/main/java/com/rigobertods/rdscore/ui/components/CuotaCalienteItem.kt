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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.CuotaCaliente
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.core.ui.VerdeDebil
import com.rigobertods.rdscore.core.ui.VerdeFuerte
import com.rigobertods.rdscore.core.ui.VerdeMedio
import com.rigobertods.rdscore.ui.util.traducirMercado
import com.rigobertods.rdscore.ui.util.traducirPrediccion
import java.util.Locale

@Composable
fun CuotaCalienteItem (
    onClick: () -> Unit,
    partido: Partido,
    cuotaCaliente: CuotaCaliente
) {
    val pick = cuotaCaliente.pick
    val colorBanner = VerdeFuerte
    if (pick.value >= 0.30) {
        VerdeFuerte.copy(alpha = 0.2f)
    } else if (pick.value in 0.20..<0.3){
        VerdeMedio.copy(alpha = 0.2f)
    } else if (pick.value in 0.10..<0.2) {
        VerdeDebil.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = partido.estadisticasLocal.liga.logo,
                            contentDescription = stringResource(R.string.cd_team_logo, partido.nombreEquipoLocal),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = cuotaCaliente.liga,
                            style = MaterialTheme.typography.titleMedium,
                        )

                    }
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
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.pick_label),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
                )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.market_format, traducirMercado(pick.mercado)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.prediction_format, traducirPrediccion(pick.prediccion)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.odds_format, pick.cuota),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.probability_format, String.format(Locale.getDefault(), "%.2f", pick.prob * 100)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colorBanner.copy(alpha = 0.2f),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.value_format, String.format(Locale.getDefault(), "%.2f", pick.value * 100)),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorBanner,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
