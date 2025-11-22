package com.linhphan.lpcore.di

import com.linhphan.lpcore.data.forecast.ForecastRepositoryImpl
import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSource
import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSourceImpl
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSource
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSourceImpl
import com.linhphan.lpcore.domain.repository.ForecastRepository
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

    @Binds
    @Singleton
    abstract fun bindForecastRemoteDataSource(
        dataSourceImpl: ForecastRemoteDataSourceImpl
    ): ForecastRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindForecastLocalDataSource(
        dataSourceImpl: ForecastLocalDataSourceImpl
    ): ForecastLocalDataSource
}