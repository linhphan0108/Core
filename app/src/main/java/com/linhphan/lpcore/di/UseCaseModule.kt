package com.linhphan.lpcore.di

import com.linhphan.lpcore.domain.usecase.GetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetForecastUseCase(
        getForecastUseCase: GetForecastUseCase
    ): IGetForecastUseCase
}