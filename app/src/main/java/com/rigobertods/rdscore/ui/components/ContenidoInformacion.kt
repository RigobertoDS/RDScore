package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.Partido

@Composable
fun ContenidoInformacion(partido: Partido) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            InfoRow(stringResource(R.string.info_date), partido.fecha)
            InfoRow(stringResource(R.string.info_time), partido.hora)
            InfoRow(stringResource(R.string.info_stadium), partido.estadio)
            InfoRow(stringResource(R.string.info_city), partido.ciudad)
        }
    }
}
