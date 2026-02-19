@file:Suppress("unused")

package com.rigobertods.rdscore.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // SessionManager is now provided via its @Inject constructor
}
