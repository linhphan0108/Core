package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import javax.inject.Inject

interface ForecastRemoteDataSource {
    suspend fun getCurrentForecast(lat: Double, lon: Double, timezone: String): Result<CurrentForecast>
    suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String?, endDate: String?): Result<HourlyForecasts>
}

class ForecastRemoteDataSourceImpl @Inject constructor(
    private val apiService: ForecastApiService,
    private val mapper: ForecastMapper
) : ForecastRemoteDataSource {

    override suspend fun getCurrentForecast(lat: Double, lon: Double, timezone: String): Result<CurrentForecast> {
        return try {
            val response = apiService.getCurrentForecast(
                lat = lat,
                lon = lon,
                timezone = timezone,
            )
            mapper.mapToDomain(response)?.let {
                Result.Success(it)
            } ?: Result.Error(Exception("No data found"))

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getHourlyForecast(lat: Double, lon: Double, timezone: String, startDate: String?, endDate: String?): Result<HourlyForecasts> {
        return try {
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
