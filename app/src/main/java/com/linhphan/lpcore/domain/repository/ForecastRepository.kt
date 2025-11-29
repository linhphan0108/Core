package com.linhphan.lpcore.domain.repository

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun getForecast(lat: Double, lon: Double): Flow<Result<HourlyForecasts>>
    suspend fun getCurrentForecast(lat: Double, lon: Double, timezone: String, startDate: String? = null, endDate: String? = null): Result<CurrentForecast>
    suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String? = null, endDate: String? = null): Result<HourlyForecasts>
}
