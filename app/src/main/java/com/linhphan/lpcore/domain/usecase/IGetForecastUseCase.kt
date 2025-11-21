package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts

interface IGetForecastUseCase {
    suspend operator fun invoke(parameters: Params): Result<Forecasts>

    data class Params(val lat: Double, val lon: Double)
}