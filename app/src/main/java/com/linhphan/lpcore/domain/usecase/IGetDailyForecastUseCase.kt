package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.ISuspendUseCase
import com.linhphan.lpcore.domain.model.DailyForecasts

interface IGetDailyForecastUseCase: ISuspendUseCase<IGetDailyForecastUseCase.Params, DailyForecasts> {

    data class Params(
        val lat: Double,
        val lon: Double,
        val timezone: String,
        val startDate: String?,
        val endDate: String?
    )
}
