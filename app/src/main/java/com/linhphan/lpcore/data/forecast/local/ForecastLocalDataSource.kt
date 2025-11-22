package com.linhphan.lpcore.data.forecast.local

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ForecastLocalDataSource {
    fun getForecast(): Flow<Forecasts?>
    suspend fun saveForecast(forecasts: Forecasts)
}

class ForecastLocalDataSourceImpl @Inject constructor(
    private val forecastDao: ForecastDao
) : ForecastLocalDataSource {

    override fun getForecast(): Flow<Forecasts?> {
        return forecastDao.getAllForecasts().map { entities ->
            if (entities.isEmpty()) {
                null
            } else {
                val first = entities.first()
                val cityInfo = CityInfo(first.cityName, first.country)
                val forecastList = entities.map {
                    Forecast(
                        date = it.date,
                        tempDay = it.tempDay,
                        tempMin = it.tempMin,
                        tempMax = it.tempMax,
                        weatherDescription = it.weatherDescription,
                        icon = it.icon
                    )
                }
                Forecasts(cityInfo, forecastList)
            }
        }
    }

    override suspend fun saveForecast(forecasts: Forecasts) {
        try {
            forecastDao.clearAll()
            val entities = forecasts.forecasts.map {
                ForecastEntity(
                    cityName = forecasts.city.name,
                    country = forecasts.city.country,
                    date = it.date,
                    tempDay = it.tempDay,
                    tempMin = it.tempMin,
                    tempMax = it.tempMax,
                    weatherDescription = it.weatherDescription,
                    icon = it.icon
                )
            }
            forecastDao.insertAll(entities)
        } catch (e: Exception) {
            // Log error or handle appropriately
        }
    }
}