package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.data.forecast.remote.ForecastApiService
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.model.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val API_KEY = "7012468a391221aa6b24073eb75e16a3"

class ForecastRepositoryImpl @Inject constructor(
    private val apiService: ForecastApiService,
    private val mapper: ForecastMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override fun getForecast(lat: Double, lon: Double): Flow<Result<Forecast>> = flow {
        emit(Result.Loading)
        try {
            // In a real app, you would check local storage first or use a mediator
            // For this example, we fetch from remote directly
            val response = apiService.getThreeHourIntervalForecast(lat = lat, lon = lon, apiKey = API_KEY)
            val forecast = mapper.mapToDomain(response)
            emit(Result.Success(forecast))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(ioDispatcher)
}