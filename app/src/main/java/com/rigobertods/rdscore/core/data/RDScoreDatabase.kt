package com.rigobertods.rdscore.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rigobertods.rdscore.core.data.dao.PartidoDao
import com.rigobertods.rdscore.core.data.entities.PartidoEntity

@Database(entities = [PartidoEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RDScoreDatabase : RoomDatabase() {
    abstract fun partidoDao(): PartidoDao
}
