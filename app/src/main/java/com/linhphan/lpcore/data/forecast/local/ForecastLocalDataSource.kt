package com.linhphan.lpcore.data.forecast.local

import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ForecastLocalDataSource {
    fun getForecast(): Flow<HourlyForecasts?>
    suspend fun saveForecast(hourlyForecasts: HourlyForecasts)
}

private const val NUMBER_OF_FORECASTS_TO_KEEP = 40 //todo: move to config


class ForecastLocalDataSourceImpl @Inject constructor(
    private val forecastDao: ForecastDao
) : ForecastLocalDataSource {

    override fun getForecast(): Flow<HourlyForecasts?> {
        return forecastDao.getAllForecasts().map { entities ->
            if (entities.isEmpty()) {
                null
            } else {
                val first = entities.first()
                val cityInfo = CityInfo(first.cityName, first.country)
                val hourlyForecastList = entities.sortedBy { it.id }.map {
                    HourlyForecast(
                        date = it.date,
                        tempDay = it.tempDay,
                        tempMin = it.tempMin,
                        tempMax = it.tempMax,
                        apparentTemperature = it.apparentTemperature,
                        weatherCondition = WeatherCondition.fromCode(it.weatherCode),
                        icon = it.icon,
                        precipitationProbability = it.precipitationProbability
                    )
                }
                HourlyForecasts(cityInfo, hourlyForecastList)
            }
        }
    }

    override suspend fun saveForecast(hourlyForecasts: HourlyForecasts) {
        try {
            val entities = hourlyForecasts.hourlyForecasts.sortedBy { it.date }
                .mapIndexed { index, forecast ->
                    ForecastEntity(
                        id = index.toLong(),
                        cityName = hourlyForecasts.city.name,
                        country = hourlyForecasts.city.country,
                        date = forecast.date,
                        tempDay = forecast.tempDay,
                        tempMin = forecast.tempMin,
                        tempMax = forecast.tempMax,
                        apparentTemperature = forecast.apparentTemperature,
                        weatherCode = forecast.weatherCondition.code,
                        icon = forecast.icon,
                        precipitationProbability = forecast.precipitationProbability
                    )
                }
            forecastDao.insertForecastsWithLimit(entities, NUMBER_OF_FORECASTS_TO_KEEP)
        } catch (e: Exception) {
            // Log error or handle appropriately
        }
    }
}