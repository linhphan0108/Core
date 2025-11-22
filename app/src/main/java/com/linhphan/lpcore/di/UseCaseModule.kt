package com.linhphan.lpcore.di

import com.linhphan.lpcore.domain.usecase.GetForecastUseCaseBase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IRefreshForecastUseCase
import com.linhphan.lpcore.domain.usecase.RefreshForecastUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetForecastUseCase(
        getForecastUseCase: GetForecastUseCaseBase
    ): IGetForecastUseCase

    @Binds
    abstract fun bindRefreshForecastUseCase(
        refreshForecastUseCase: RefreshForecastUseCase
    ): IRefreshForecastUseCase
}