package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.remote.ForecastApiService
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val API_KEY = "7012468a391221aa6b24073eb75e16a3"

class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ForecastApiService,
    private val mapper: ForecastMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override suspend fun getForecast(lat: Double, lon: Double): Result<Forecasts> = withContext(ioDispatcher) {
        return@withContext try {
            // In a real app, you would check local storage first or use a mediator
            // For this example, we fetch from remote directly
            val response = apiService.getThreeHourIntervalForecast(lat = lat, lon = lon, apiKey = API_KEY)
            val forecast = mapper.mapToDomain(response)
            Result.Success(forecast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}