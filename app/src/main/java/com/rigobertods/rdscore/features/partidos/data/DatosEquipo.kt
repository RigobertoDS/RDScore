package com.rigobertods.rdscore.features.partidos.data

import com.google.gson.annotations.SerializedName

data class DatosEquipo (
    @SerializedName("partidos")
    val partidosEquipo: List<PartidosEquipo> = listOf(),
    @SerializedName("equipos")
    val equiposLigaEquipo: List<EquiposLiga> = listOf()
)

data class PartidosEquipo(
    @SerializedName("id_liga")
    val idLiga: Int,
    @SerializedName("partidos")
    val partidosLiga: List<Partido>
)

data class EquiposLiga(
    @SerializedName("id_liga")
    val idLiga: Int,
    @SerializedName("equipo")
    val equipo: Equipo,
    @SerializedName("equipos")
    val equiposLiga: List<Equipo>
)
