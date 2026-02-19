package com.rigobertods.rdscore.features.partidos.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Partido(
    @SerializedName("id_partido")
    val idPartido: Int,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("id_liga")
    val idLiga: Int,

    @SerializedName("temporada")
    val temporada: Int,

    @SerializedName("jornada")
    val jornada: String,

    @SerializedName("equipo_local")
    val nombreEquipoLocal: String,

    @SerializedName("equipo_visitante")
    val nombreEquipoVisitante: String,

    @SerializedName("estadisticas_local")
    val estadisticasLocal: Equipo,

    @SerializedName("estadisticas_visitante")
    val estadisticasVisitante: Equipo,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("hora")
    val hora: String,

    @SerializedName("ciudad")
    val ciudad: String,

    @SerializedName("estadio")
    val estadio: String,

    @SerializedName("arbitro")
    val arbitro: String,

    @SerializedName("cuota_local")
    val cuotaLocal: Double,

    @SerializedName("cuota_empate")
    val cuotaEmpate: Double,

    @SerializedName("cuota_visitante")
    val cuotaVisitante: Double,

    @SerializedName("cuota_over")
    val cuotaOver: Double,

    @SerializedName("cuota_under")
    val cuotaUnder: Double,

    @SerializedName("cuota_btts")
    val cuotaBtts: Double,

    @SerializedName("cuota_btts_no")
    val cuotaBttsNo: Double,

    @SerializedName("goles_local")
    val golesLocal: Int,

    @SerializedName("goles_visitante")
    val golesVisitante: Int,

    @SerializedName("resultado")
    val resultado: Int,

    @SerializedName("ambos_marcan")
    val ambosMarcan: Int,

    @SerializedName("local_marca")
    val localMarca: Int,

    @SerializedName("visitante_marca")
    val visitanteMarca: Int,

    @SerializedName("mas_2_5")
    val mas25: Int,

    @SerializedName("prediccion")
    val prediccion: TipoPrediccion
) : Parcelable
