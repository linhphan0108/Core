package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSource
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSource
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: ForecastRemoteDataSource,
    private val localDataSource: ForecastLocalDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override fun getForecast(lat: Double, lon: Double): Flow<Result<Forecasts>> {
        return localDataSource.getForecast()
            .distinctUntilChanged()
            .map { forecast ->
                if (forecast != null) {
                    Timber.i("${forecast.forecasts.size} forecasts fetched from local DB")
                    Result.Success(forecast)
                } else {
                    Result.Error(Exception("No local data found"))
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun refreshForecast(lat: Double, lon: Double): Result<Unit> = withContext(ioDispatcher) {
        try {
            when (val remoteResult = remoteDataSource.getForecast(lat, lon)) {
                is Result.Success -> {
                    localDataSource.saveForecast(remoteResult.data)
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Result.Error(remoteResult.exception)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}