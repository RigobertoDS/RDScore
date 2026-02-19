package com.rigobertods.rdscore.features.equipo.data

import com.google.gson.annotations.SerializedName
import com.rigobertods.rdscore.features.partidos.data.DatosEquipo

data class DatosEquipoResponse (
    @SerializedName("datos")
    val datosEquipo: DatosEquipo,
)
