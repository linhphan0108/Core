package com.linhphan.lpcore.di

import com.linhphan.lpcore.data.forecast.ForecastRepository
import com.linhphan.lpcore.data.forecast.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(
        repositoryImpl: ForecastRepositoryImpl
    ): ForecastRepository
}