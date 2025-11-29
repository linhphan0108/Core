package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSource
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSource
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: ForecastRemoteDataSource,
    private val localDataSource: ForecastLocalDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override fun getForecast(lat: Double, lon: Double): Flow<Result<HourlyForecasts>> {
        return localDataSource.getForecast()
            .distinctUntilChanged()
            .map { forecast ->
                if (forecast != null) {
                    Timber.i("${forecast.hourlyForecasts.size} forecasts fetched from local DB")
                    Result.Success(forecast)
                } else {
                    Result.Error(Exception("No local data found"))
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun getCurrentForecast(
        lat: Double,
        lon: Double,
        timezone: String,
        startDate: String?,
        endDate: String?
    ): Result<CurrentForecast> {
        return remoteDataSource.getCurrentForecast(lat, lon, timezone)
    }

    override suspend fun getHourlyForecast(
        lat: Double,
        lon: Double,
        timezone: String,
        startDate: String?,
        endDate: String?,
    ): Result<HourlyForecasts> {
        return remoteDataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate)
    }
}
