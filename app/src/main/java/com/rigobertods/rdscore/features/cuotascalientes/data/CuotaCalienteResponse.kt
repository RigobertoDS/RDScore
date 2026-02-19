package com.rigobertods.rdscore.features.cuotascalientes.data

import com.google.gson.annotations.SerializedName
import com.rigobertods.rdscore.features.partidos.data.CuotaCaliente
import com.rigobertods.rdscore.features.partidos.data.Partido

data class CuotaCalienteResponse (
    @SerializedName("partidos")
    val partidosList: List<Partido>,
    @SerializedName("cuotas_calientes")
    val cuotasCalientes: List<CuotaCaliente>
)
