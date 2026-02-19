package com.rigobertods.rdscore

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.features.partidos.data.Btts
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.GolesEsperados
import com.rigobertods.rdscore.features.partidos.data.Liga
import com.rigobertods.rdscore.features.partidos.data.Over25
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.Prediccion
import com.rigobertods.rdscore.features.partidos.data.Probabilidades
import com.rigobertods.rdscore.features.partidos.data.Recomendacion
import com.rigobertods.rdscore.features.partidos.data.Resultado1x2
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion
import com.rigobertods.rdscore.features.partidos.ui.PantallaDetallePartido
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun PantallaDetallePartidoPreview() {
    val equipoEjemplo = Equipo(
        0,
        "",
        "",
        0,
        0,
        0.0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        "",
        0,
        0.0,
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0.0,
        0,
        "",
        Liga(0, "La Liga", "España", "", "")
    )
    val prediccionEjemplo = Prediccion(
        GolesEsperados(1.5, 0.5),
        Resultado1x2("Local", Probabilidades(0.7, 0.2, 0.1), 0.7, Recomendacion(1, 1, 1)),
        Btts("", 0.6, Recomendacion(1, 1, 1)),
        Over25("", 0.4, Recomendacion(1, 1, 1))
    )
    val tipoPrediccionEjemplo = TipoPrediccion.Detallada(prediccionEjemplo)
    val partidoEjemplo = Partido(
        1,
        "FT",
        1,
        2025,
        "J1",
        "Equipo A",
        "Equipo B",
        equipoEjemplo,
        equipoEjemplo,
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        "21:00",
        "Madrid",
        "Estadio",
        "Arbitro",
        1.0,
        1.0,
        1.0,
        1.0,
        1.0,
        1.0,
        1.0,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        tipoPrediccionEjemplo)
    PantallaDetallePartido(partidoEjemplo)
}
