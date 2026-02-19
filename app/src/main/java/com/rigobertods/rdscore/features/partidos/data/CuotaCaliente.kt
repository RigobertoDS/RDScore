package com.rigobertods.rdscore.features.partidos.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Clase principal para cada "cuota caliente"
data class CuotaCaliente(
    @SerializedName("id")
    val id: Int,

    @SerializedName("liga")
    val liga: String,

    @SerializedName("partido")
    val partido: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("pick")
    val pick: Oportunidad
) : Serializable

// Clase para cada oportunidad dentro del partido
data class Oportunidad(
    @SerializedName("mercado")
    val mercado: String,

    @SerializedName("prediccion")
    val prediccion: String,

    @SerializedName("prob")
    val prob: Double,

    @SerializedName("cuota")
    val cuota: Double,

    @SerializedName("value")
    val value: Double,

    @SerializedName("score")
    val score: Double
) : Serializable
