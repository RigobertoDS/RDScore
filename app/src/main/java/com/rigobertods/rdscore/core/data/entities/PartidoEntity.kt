package com.rigobertods.rdscore.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion

@Entity(tableName = "partidos")
data class PartidoEntity(
    @PrimaryKey
    val idPartido: Int,
    val estado: String,
    val idLiga: Int,
    val temporada: Int,
    val jornada: String,
    val nombreEquipoLocal: String,
    val nombreEquipoVisitante: String,
    val estadisticasLocal: Equipo,
    val estadisticasVisitante: Equipo,
    val fecha: String,
    val hora: String,
    val ciudad: String,
    val estadio: String,
    val arbitro: String,
    val cuotaLocal: Double,
    val cuotaEmpate: Double,
    val cuotaVisitante: Double,
    val cuotaOver: Double,
    val cuotaUnder: Double,
    val cuotaBtts: Double,
    val cuotaBttsNo: Double,
    val golesLocal: Int,
    val golesVisitante: Int,
    val resultado: Int,
    val ambosMarcan: Int,
    val localMarca: Int,
    val visitanteMarca: Int,
    val mas25: Int,
    val prediccion: TipoPrediccion,
    val cacheDate: String // To group by date in local query
)
