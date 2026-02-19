package com.rigobertods.rdscore.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.core.ui.RDScoreTheme
import java.time.LocalDate

@Preview(
    name = "Modo Claro",
    showBackground = true
)
@Preview(
    name = "Modo Noche",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BarraNavegacionFlechasPreview () {
    val fechaHoy = LocalDate.now()

    RDScoreTheme(darkTheme = false) {
        BarraNavegacionFechas(
            fecha = fechaHoy,
            enabled = true,
            onFechaAnterior = {},
            onFechaSiguiente = {},
            onCalendarioClick = {},
            onFiltroClick = {}
        )
    }
}
