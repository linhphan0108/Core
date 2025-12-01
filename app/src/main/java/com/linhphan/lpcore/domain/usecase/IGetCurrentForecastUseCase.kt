package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.ISuspendUseCase
import com.linhphan.lpcore.domain.model.CurrentForecast

interface IGetCurrentForecastUseCase : ISuspendUseCase<IGetCurrentForecastUseCase.Params, CurrentForecast> {
    data class Params(
        val lat: Double,
        val lon: Double,
        val timezone: String,
        val startDate: String? = null,
        val endDate: String? = null
    )
}
