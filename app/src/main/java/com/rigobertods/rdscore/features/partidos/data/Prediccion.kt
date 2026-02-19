package com.rigobertods.rdscore.features.partidos.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Estructura para el objeto 'recomendacion' que es común en varias predicciones.
@Parcelize
data class Recomendacion(
    @SerializedName("arriesgada")
    val arriesgada: Int,
    @SerializedName("conservadora")
    val conservadora: Int,
    @SerializedName("moderada")
    val moderada: Int
) : Parcelable

// Contenedor principal de la predicción, tal como está en el JSON.
@Parcelize
data class Prediccion(
    @SerializedName("goles_esperados")
    val golesEsperados: GolesEsperados,
    @SerializedName("resultado_1x2")
    val resultado1x2: Resultado1x2,
    @SerializedName("btts")
    val btts: Btts,
    @SerializedName("over25")
    val over25: Over25
) : Parcelable

// Representa el objeto 'goles_esperados'
@Parcelize
data class GolesEsperados(
    @SerializedName("local")
    val local: Double,
    @SerializedName("visitante")
    val visitante: Double
) : Parcelable


// Representa el objeto 'resultado_1x2'
@Parcelize
data class Resultado1x2(
    @SerializedName("prediccion")
    val prediccion: String,
    @SerializedName("probabilidades")
    val probabilidades: Probabilidades,
    @SerializedName("probabilidad_max")
    val probabilidadMax: Double,
    @SerializedName("recomendacion")
    val recomendacion: Recomendacion
) : Parcelable


// Representa el objeto 'probabilidades' dentro de 'resultado_1x2'
@Parcelize
data class Probabilidades(
    @SerializedName("local")
    val local: Double,
    @SerializedName("empate")
    val empate: Double,
    @SerializedName("visitante")
    val visitante: Double
) : Parcelable

// Representa el objeto 'btts' (Ambos equipos marcan)
@Parcelize
data class Btts(
    @SerializedName("prediccion")
    val prediccion: String,
    @SerializedName("probabilidad")
    val probabilidad: Double,
    @SerializedName("recomendacion")
    val recomendacion: Recomendacion
) : Parcelable

// Representa el objeto 'over25' (Más/Menos de 2.5 goles)
@Parcelize
data class Over25(
    @SerializedName("prediccion")
    val prediccion: String,
    @SerializedName("probabilidad")
    val probabilidad: Double,
    @SerializedName("recomendacion")
    val recomendacion: Recomendacion
) : Parcelable
