package com.rigobertods.rdscore.features.resumen.data

import com.google.gson.annotations.SerializedName
import com.rigobertods.rdscore.features.partidos.data.ResumenMercado

data class PrecisionMercadoResponse (
    @SerializedName("mensaje")
    val mensaje: ResumenMercado
)
