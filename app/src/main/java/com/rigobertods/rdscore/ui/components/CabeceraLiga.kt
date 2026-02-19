package com.rigobertods.rdscore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.Partido

import com.rigobertods.rdscore.features.ligas.data.Liga

@Composable
fun CabeceraLiga(
    partido: Partido,
    ligaInfo: Liga? = null,
    pagina: Int,
    estaExpandida: Boolean,
    onClick: () -> Unit,
    partidosDeLaLiga: List<Partido>,
    onPartidoClick: (Partido) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ligaInfo?.logo ?: partido.estadisticasLocal.liga.logo,
                    contentDescription = stringResource(R.string.cd_league_logo, ligaInfo?.nombre ?: partido.estadisticasLocal.liga.nombre),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = ligaInfo?.nombre ?: partido.estadisticasLocal.liga.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                val anguloRotacion by animateFloatAsState(
                    targetValue = if (estaExpandida) 180f else 0f,
                    label = "rotation"
                )

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (estaExpandida) "Contraer" else "Expandir",
                    modifier = Modifier.rotate(anguloRotacion)
                )
            }
            AnimatedVisibility(visible = estaExpandida) {
                Column {
                    partidosDeLaLiga.forEach { partido ->
                        PartidoItem(
                            partido = partido,
                            pagina = pagina,
                            onClick = { onPartidoClick(partido) }
                        )
                    }
                }
            }
        }
    }
}
