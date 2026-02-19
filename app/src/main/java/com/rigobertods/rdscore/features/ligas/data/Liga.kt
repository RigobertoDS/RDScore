package com.rigobertods.rdscore.features.ligas.data

import com.google.gson.annotations.SerializedName

data class LigaResponse(
    @SerializedName("ligas")
    val ligas: List<Liga>
)

data class Liga(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("pais")
    val pais: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("bandera")
    val bandera: String?
)
