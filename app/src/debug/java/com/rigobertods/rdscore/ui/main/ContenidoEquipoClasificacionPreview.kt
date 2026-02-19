package com.rigobertods.rdscore.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.Liga
import com.rigobertods.rdscore.ui.components.ContenidoEquipoClasificacion
import com.rigobertods.rdscore.core.ui.RDScoreTheme

@Preview(showBackground = true)
@Composable
fun ContenidoEquipoClasificacionPreview () {
    val equipo1 = Equipo(
        1, "Atleti", "", 1, 50, 0.0, 10, 10,
        10, 10, 0, 0, 0, 0, 0, 0, 0, 0, "",
        0, 0.0, 0, 0.0, 0, 0, 0.0, 0, 0.0,
        0, 0, 0.0, 0, 0.0, 0, "", Liga()
    )

    val equipo2 = Equipo(
        2, "Barsa", "", 2, 45, 0.0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "",
        0, 0.0, 0, 0.0, 0, 0, 0.0, 0, 0.0,
        0, 0, 0.0, 0, 0.0, 0, "", Liga()
    )

    val equipo3 = Equipo(
        3, "Madrid", "", 3, 44, 0.0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "",
        0, 0.0, 0, 0.0, 0, 0, 0.0, 0, 0.0,
        0, 0, 0.0, 0, 0.0, 0, "", Liga()
    )

    val equipos = listOf(equipo1, equipo2, equipo3)

    RDScoreTheme {
        ContenidoEquipoClasificacion(equipo1, equipos)
    }
}
