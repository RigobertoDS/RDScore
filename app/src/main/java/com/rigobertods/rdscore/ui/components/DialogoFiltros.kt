package com.rigobertods.rdscore.ui.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.R

@Composable
fun DialogoFiltros(
    filtroState: FiltroState,
    onDismiss: () -> Unit,
    onAplicarFiltros: (FiltroState) -> Unit
) {
    var modoAvanzado by remember { mutableStateOf(false) }
    var estadoTemporal by remember { mutableStateOf(filtroState) }
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    val titulosTabs = listOf(
        stringResource(R.string.tab_conservative),
        stringResource(R.string.tab_moderate),
        stringResource(R.string.tab_aggressive)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (modoAvanzado) stringResource(R.string.advanced_filters) else stringResource(R.string.quick_filters))
            }
        },
        text = {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!modoAvanzado) {
                    // Botón para IR a la vista avanzada
                    TextButton(
                        onClick = { modoAvanzado = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FilterAlt,
                            contentDescription = stringResource(R.string.advanced_filters),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.open_advanced_filters),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = stringResource(R.string.open_advanced_filters),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    FilaFiltro(
                        texto = stringResource(R.string.select_deselect_all),
                        seleccionado = estadoTemporal.esTodoMarcado(),
                        onCheckedChange = {
                            estadoTemporal = if (it) {
                                estadoTemporal.marcarTodo()
                            } else {
                                estadoTemporal.desmarcarTodo()
                            }
                        }
                    )
                    FilaFiltro(
                        texto = stringResource(R.string.result_1x2),
                        seleccionado = estadoTemporal.esSoloResultadoMarcado(),
                        onCheckedChange = {
                            estadoTemporal = if (it) {
                                estadoTemporal.marcarSoloResultado()
                            } else {
                                estadoTemporal.desmarcarSoloResultado()
                            }
                        }
                    )
                    FilaFiltro(
                        texto = stringResource(R.string.both_teams_score),
                        seleccionado = estadoTemporal.esSoloBTTSMarcado(),
                        onCheckedChange = {
                            estadoTemporal = if (it) {
                                estadoTemporal.marcarSoloBTTS()
                            } else {
                                estadoTemporal.desmarcarSoloBTTS()
                            }
                        }
                    )
                    FilaFiltro(
                        texto = stringResource(R.string.over_under_25),
                        seleccionado = estadoTemporal.esSoloOverUnderMarcado(),
                        onCheckedChange = {
                            estadoTemporal = if (it) {
                                estadoTemporal.marcarSoloOverUnder()
                            } else {
                                estadoTemporal.desmarcarSoloOverUnder()
                            }
                        }
                    )
                } else {
                    TextButton(
                        onClick = { modoAvanzado = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FilterAlt,
                            contentDescription = stringResource(R.string.quick_filters),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.back_to_quick_filters),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = stringResource(R.string.back_to_quick_filters),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    // Selector de pestañas por Modelo
                    PrimaryTabRow(
                        selectedTabIndex = tabSeleccionada,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabSeleccionada),
                                height = 3.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    ) {
                        titulosTabs.forEachIndexed { index, titulo ->
                            Tab(
                                selected = tabSeleccionada == index,
                                onClick = { tabSeleccionada = index },
                                text = { Text(titulo, fontSize = 12.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Contenido filtrado por la pestaña seleccionada
                    Column {
                        when (tabSeleccionada) {
                            0 -> { // Conservador
                                FilaFiltro(
                                    texto = stringResource(R.string.result_1x2),
                                    seleccionado = estadoTemporal.resultadoConservador,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(resultadoConservador = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.both_teams_score),
                                    seleccionado = estadoTemporal.bttsConservador,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(bttsConservador = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.over_under_25),
                                    seleccionado = estadoTemporal.overUnderConservador,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(overUnderConservador = it) }
                                )
                            }
                            1 -> { // Moderado
                                FilaFiltro(
                                    texto = stringResource(R.string.result_1x2),
                                    seleccionado = estadoTemporal.resultadoModerado,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(resultadoModerado = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.both_teams_score),
                                    seleccionado = estadoTemporal.bttsModerado,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(bttsModerado = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.over_under_25),
                                    seleccionado = estadoTemporal.overUnderModerado,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(overUnderModerado = it) }
                                )
                            }
                            2 -> { // Arriesgado
                                FilaFiltro(
                                    texto = stringResource(R.string.result_1x2),
                                    seleccionado = estadoTemporal.resultadoAgresivo,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(resultadoAgresivo = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.both_teams_score),
                                    seleccionado = estadoTemporal.bttsAgresivo,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(bttsAgresivo = it) }
                                )
                                FilaFiltro(
                                    texto = stringResource(R.string.over_under_25),
                                    seleccionado = estadoTemporal.overUnderAgresivo,
                                    onCheckedChange = { estadoTemporal = estadoTemporal.copy(overUnderAgresivo = it) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAplicarFiltros(estadoTemporal) }) {
                Text(stringResource(R.string.apply), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

@Composable
fun FilaFiltro(texto: String, seleccionado: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onCheckedChange(!seleccionado) }
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(checked = seleccionado, onCheckedChange = onCheckedChange)
        Spacer(Modifier.width(8.dp))
        Text(texto, style = MaterialTheme.typography.bodyLarge)
    }
}
