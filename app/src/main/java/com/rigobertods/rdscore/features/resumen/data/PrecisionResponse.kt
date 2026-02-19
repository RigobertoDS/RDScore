package com.rigobertods.rdscore.features.resumen.data

import com.google.gson.annotations.SerializedName
import com.rigobertods.rdscore.features.partidos.data.Resumen

data class PrecisionResponse (
    @SerializedName("mensaje")
    val mensaje: Resumen
)
