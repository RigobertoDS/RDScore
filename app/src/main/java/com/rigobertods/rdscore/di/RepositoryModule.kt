package com.rigobertods.rdscore.di

import com.rigobertods.rdscore.features.partidos.data.PartidosRepository
import com.rigobertods.rdscore.core.data.dao.PartidoDao
import com.rigobertods.rdscore.features.resumen.data.StatsRepository
import com.rigobertods.rdscore.features.equipo.data.TeamRepository
import com.rigobertods.rdscore.features.perfil.data.UserRepository
import com.rigobertods.rdscore.features.auth.data.AuthRepository
import com.rigobertods.rdscore.core.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePartidosRepository(
        apiService: ApiService,
        partidoDao: PartidoDao
    ): PartidosRepository {
        return PartidosRepository(apiService, partidoDao)
    }

    @Provides
    @Singleton
    fun provideStatsRepository(apiService: ApiService): StatsRepository {
        return StatsRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(
        apiService: ApiService,
        partidosRepository: PartidosRepository
    ): TeamRepository {
        return TeamRepository(apiService, partidosRepository)
    }
}
