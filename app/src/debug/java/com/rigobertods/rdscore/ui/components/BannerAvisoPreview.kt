package com.rigobertods.rdscore.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun BannerAvisoPreview () {
    BannerAviso(
        titulo = "Mercado Resultado temporalmente desactivado",
        texto = "El modelo de predicción del mercado Resultado se encuentra en proceso de recalibración.\n" +
                "Los pronósticos se volverán a habilitar una vez finalizado el ajuste."
    )
}
