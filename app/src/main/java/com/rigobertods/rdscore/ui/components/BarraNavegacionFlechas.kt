package com.rigobertods.rdscore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacionFechas(
    fecha: LocalDate,
    enabled: Boolean,
    onFechaAnterior: () -> Unit,
    onFechaSiguiente: () -> Unit,
    onCalendarioClick: () -> Unit,
    onFiltroClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.getDefault())
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = { Text(fecha.format(formatter), fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onFechaAnterior, enabled = enabled) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_previous_day))
            }
        },
        actions = {
            IconButton(onClick = onFiltroClick, enabled = enabled) {
                Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.cd_filter_matches))
            }
            IconButton(onClick = onCalendarioClick, enabled = enabled) {
                Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.cd_select_date))
            }
            IconButton(onClick = onFechaSiguiente, enabled = enabled) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = stringResource(R.string.cd_next_day))
            }
        }
    )
}
