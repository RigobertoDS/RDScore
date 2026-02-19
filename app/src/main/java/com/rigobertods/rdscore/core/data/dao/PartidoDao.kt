package com.rigobertods.rdscore.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rigobertods.rdscore.core.data.entities.PartidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoDao {
    @Query("SELECT * FROM partidos WHERE cacheDate = :fecha ORDER BY hora ASC")
    fun getPartidosPorFecha(fecha: String): Flow<List<PartidoEntity>>

    @Query("SELECT * FROM partidos WHERE cacheDate = :fecha ORDER BY hora ASC")
    suspend fun getPartidosPorFechaOnce(fecha: String): List<PartidoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartidos(partidos: List<PartidoEntity>)

    @Query("DELETE FROM partidos WHERE cacheDate = :fecha")
    suspend fun deletePartidosPorFecha(fecha: String)

    @Query("DELETE FROM partidos")
    suspend fun clearAll()

    @Query("SELECT * FROM partidos WHERE idPartido = :id")
    suspend fun getPartidoById(id: Int): PartidoEntity?
}
