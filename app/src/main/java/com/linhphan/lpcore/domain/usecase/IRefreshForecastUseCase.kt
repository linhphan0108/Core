package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result

interface IRefreshForecastUseCase {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Unit>

    data class Params(val lat: Double, val lon: Double)
}