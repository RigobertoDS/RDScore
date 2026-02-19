package com.rigobertods.rdscore.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.core.ui.Rojo
import com.rigobertods.rdscore.core.ui.VerdeFuerte
import com.rigobertods.rdscore.ui.util.obtenerRecomendacion

@Composable
fun RecomendacionRow(titulo: String, valor: Int) {

    val texto = obtenerRecomendacion(valor)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            titulo,
            modifier = Modifier
                .fillMaxWidth().weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            texto,
            color = when (valor) {
                1 -> VerdeFuerte
                0 -> Rojo
                else -> {
                    return
                }
            }
        )
    }
}
