package com.linhphan.lpcore.domain.repository

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun getForecast(lat: Double, lon: Double): Flow<Result<Forecasts>>
    suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String? = null, endDate: String? = null): Result<Forecasts>
}
