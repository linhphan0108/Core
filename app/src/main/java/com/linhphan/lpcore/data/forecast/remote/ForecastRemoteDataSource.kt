package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import javax.inject.Inject

private const val API_KEY = "7012468a391221aa6b24073eb75e16a3"

interface ForecastRemoteDataSource {
    suspend fun getForecast(lat: Double, lon: Double): Result<Forecasts>
}

class ForecastRemoteDataSourceImpl @Inject constructor(
    private val apiService: ForecastApiService,
    private val mapper: ForecastMapper
) : ForecastRemoteDataSource {

    override suspend fun getForecast(lat: Double, lon: Double): Result<Forecasts> {
        return try {
            val response = apiService.getThreeHourIntervalForecast(lat = lat, lon = lon, apiKey = API_KEY)
            val forecast = mapper.mapToDomain(response)
            Result.Success(forecast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}