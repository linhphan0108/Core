package com.linhphan.lpcore.domain.repository

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts

interface ForecastRepository {
    suspend fun getForecast(lat: Double, lon: Double): Result<Forecasts>
}