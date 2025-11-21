package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.domain.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun getForecast(lat: Double, lon: Double): Flow<Result<Forecast>>
}