package com.rigobertods.rdscore.di

import android.content.Context
import androidx.room.Room
import com.rigobertods.rdscore.core.data.RDScoreDatabase
import com.rigobertods.rdscore.core.data.dao.PartidoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RDScoreDatabase {
        return Room.databaseBuilder(
            context,
            RDScoreDatabase::class.java,
            "rdscore_database"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun providePartidoDao(database: RDScoreDatabase): PartidoDao {
        return database.partidoDao()
    }
}
