package com.linhphan.lpcore.di

import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.impl.GetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.domain.usecase.impl.GetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.impl.GetHourlyForecastUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetForecastUseCase(
        getForecastUseCase: GetForecastUseCase
    ): IGetForecastUseCase

    @Binds
    abstract fun bindGetCurrentForecastUseCase(
        getCurrentForecastUseCase: GetCurrentForecastUseCase
    ): IGetCurrentForecastUseCase

    @Binds
    abstract fun bindGetHourlyForecastUseCase(
        getHourlyForecastUseCase: GetHourlyForecastUseCase
    ): IGetHourlyForecastUseCase
}
