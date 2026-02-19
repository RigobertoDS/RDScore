package com.rigobertods.rdscore.features.resumen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.core.ui.Blanco
import com.rigobertods.rdscore.core.ui.Negro
import com.rigobertods.rdscore.core.ui.Rojo
import com.rigobertods.rdscore.core.ui.VerdeFuerte
import com.rigobertods.rdscore.features.partidos.data.ModeloResumen
import com.rigobertods.rdscore.features.partidos.data.Resumen
import com.rigobertods.rdscore.features.partidos.data.ResumenMercado
import com.rigobertods.rdscore.ui.components.PaginaResumen
import com.rigobertods.rdscore.ui.util.traducirMercado
import com.rigobertods.rdscore.ui.util.traducirModelo
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaResumen(
    precision: Resumen?,
    precisionMercado: ResumenMercado?,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit
) {

    Scaffold(
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
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Negro,
                    titleContentColor = Blanco
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && precision == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else if (precision != null && precisionMercado != null) {
                Column {
                    val mejor = precisionMercado.obtenerMejorRoi()

                    // Historial card removed - integrated in main screen now


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column (
                            modifier = Modifier.padding(16.dp)
                        ){
                            Row {
                                Icon(
                                    modifier = Modifier.size(24.dp).align(Alignment.Top).fillMaxHeight(),
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = stringResource(R.string.cd_featured),
                                    tint = Color.Yellow
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = stringResource(R.string.featured_recommendation),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            Text(
                                text = stringResource(R.string.model_market_format, traducirModelo(mejor?.second ?: ""), traducirMercado(mejor?.first ?: "")),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row {
                                mejor?.third?.roi?.let {
                                    Text(
                                        text = "ROI: ${
                                            String.format(
                                                Locale.getDefault(),
                                                "%.2f",
                                                mejor.third.roi
                                            )
                                        }%",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (it > 0) VerdeFuerte else Rojo
                                    )
                                }
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = stringResource(R.string.hits_format, mejor?.third?.aciertosBrutos ?: 0, mejor?.third?.apuestas ?: 0),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    PaginaResumen(precision, precisionMercado)
                }
            }
        }
    }
}

private fun ResumenMercado.obtenerMejorRoi (): Triple<String, String, ModeloResumen>? {

    val candidatos = listOf(
        Triple("Resultado", "Agresivo", resultado.agresivo),
        Triple("Resultado", "Moderado", resultado.moderado),
        Triple("Resultado", "Conservador", resultado.conservador),

        Triple("Ambos Marcan", "Agresivo", btts.agresivo),
        Triple("Ambos Marcan", "Moderado", btts.moderado),
        Triple("Ambos Marcan", "Conservador", btts.conservador),

        Triple("Más/Menos 2.5", "Agresivo", over.agresivo),
        Triple("Más/Menos 2.5", "Moderado", over.moderado),
        Triple("Más/Menos 2.5", "Conservador", over.conservador)
    )

    val mejor = candidatos.maxByOrNull { it.third.roi }

    return mejor
}
