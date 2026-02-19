package com.rigobertods.rdscore.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun DialogoFiltrosPreview() {
    var filtros by remember { mutableStateOf(FiltroState()) }

    DialogoFiltros(
        filtroState = filtros,
        onDismiss = { },
        onAplicarFiltros = { _ ->
        }
    )
}
