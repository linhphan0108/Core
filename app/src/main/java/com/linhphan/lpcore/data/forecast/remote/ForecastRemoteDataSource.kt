package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import javax.inject.Inject

interface ForecastRemoteDataSource {
    suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String?, endDate: String?): Result<Forecasts>
}

class ForecastRemoteDataSourceImpl @Inject constructor(
    private val apiService: ForecastApiService,
    private val mapper: ForecastMapper
) : ForecastRemoteDataSource {

    override suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String?, endDate: String?): Result<Forecasts> {
        return try {
            // Open-Meteo does not require an API key
            val response = apiService.getHourlyForecast(
                lat = lat,
                lon = lon,
                timezone = timezone,
                startDate = startDate,
                endDate = endDate)
            val forecast = mapper.mapToDomain(response)
            Result.Success(forecast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
