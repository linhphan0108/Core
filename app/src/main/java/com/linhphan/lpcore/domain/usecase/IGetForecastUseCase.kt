package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.HourlyForecasts
import kotlinx.coroutines.flow.Flow

interface IGetForecastUseCase {
    operator fun invoke(parameters: Params): Flow<Result<HourlyForecasts>>

    data class Params(val lat: Double, val lon: Double)
}