package com.rigobertods.rdscore.features.partidos.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipo(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("logo")
    val logo: String = "",

    @SerializedName("posicion")
    val posicion: Int = 0,

    @SerializedName("puntos")
    val puntos: Int = 0,

    @SerializedName("forma")
    val forma: Double = 0.0,

    @SerializedName("PT")
    val partidosTotales: Int = 0,

    @SerializedName("VT")
    val victoriasTotales: Int = 0,

    @SerializedName("ET")
    val empatesTotales: Int = 0,

    @SerializedName("DT")
    val derrotasTotales: Int = 0,

    @SerializedName("PC")
    val partidosCasa: Int = 0,

    @SerializedName("VC")
    val victoriasCasa: Int = 0,

    @SerializedName("EC")
    val empatesCasa: Int = 0,

    @SerializedName("DC")
    val derrotasCasa: Int = 0,

    @SerializedName("PF")
    val partidosFuera: Int = 0,

    @SerializedName("VF")
    val victoriasFuera: Int = 0,

    @SerializedName("EF")
    val empatesFuera: Int = 0,

    @SerializedName("DF")
    val derrotasFuera: Int = 0,

    @SerializedName("temporada")
    val temporada: String = "",

    @SerializedName("goles_favor")
    val golesFavor: Int = 0,

    @SerializedName("goles_favor_por_partido")
    val golesFavorPorPartido: Double = 0.0,

    @SerializedName("goles_contra")
    val golesContra: Int = 0,

    @SerializedName("goles_contra_por_partido")
    val golesContraPorPartido: Double = 0.0,

    @SerializedName("diferencia_goles")
    val diferenciaGoles: Int = 0,

    @SerializedName("goles_favor_casa")
    val golesFavorCasa: Int = 0,

    @SerializedName("goles_favor_casa_por_partido")
    val golesFavorCasaPorPartido: Double = 0.0,

    @SerializedName("goles_contra_casa")
    val golesContraCasa: Int = 0,

    @SerializedName("goles_contra_casa_por_partido")
    val golesContraCasaPorPartido: Double = 0.0,

    @SerializedName("diferencia_goles_casa")
    val diferenciaGolesCasa: Int = 0,

    @SerializedName("goles_favor_fuera")
    val golesFavorFuera: Int = 0,

    @SerializedName("goles_favor_fuera_por_partido")
    val golesFavorFueraPorPartido: Double = 0.0,

    @SerializedName("goles_contra_fuera")
    val golesContraFuera: Int = 0,

    @SerializedName("goles_contra_fuera_por_partido")
    val golesContraFueraPorPartido: Double = 0.0,

    @SerializedName("diferencia_goles_fuera")
    val diferenciaGolesFuera: Int = 0,

    @SerializedName("ultimos_partidos")
    val ultimosPartidos: String = "",

    @SerializedName("liga")
    val liga: Liga = Liga()
) : Parcelable
