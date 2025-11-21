package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import kotlinx.coroutines.flow.Flow

interface IGetForecastUseCase {
    operator fun invoke(parameters: Params): Flow<Result<Forecasts>>

    data class Params(val lat: Double, val lon: Double)
}