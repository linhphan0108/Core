package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast

interface IGetCurrentForecastUseCase {
    data class Params(
        val lat: Double,
        val lon: Double,
        val timezone: String,
        val startDate: String? = null,
        val endDate: String? = null
    )

    suspend operator fun invoke(params: Params): Result<CurrentForecast>
}
