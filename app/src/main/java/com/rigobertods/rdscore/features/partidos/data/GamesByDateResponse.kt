package com.rigobertods.rdscore.features.partidos.data

import com.google.gson.annotations.SerializedName

data class GamesByDateResponse (
    @SerializedName("partidos")
    val partidosList: List<Partido>
)
