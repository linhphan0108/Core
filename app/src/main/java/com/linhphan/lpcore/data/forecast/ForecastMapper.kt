package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.ForecastResponseDto
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.DailyForecast
import com.linhphan.lpcore.domain.model.Forecast
import javax.inject.Inject

class ForecastMapper @Inject constructor() {
    fun mapToDomain(response: ForecastResponseDto): Forecast {
        val city = CityInfo(
            name = response.city?.name.orEmpty(),
            country = response.city?.country.orEmpty()
        )
        val list = response.list?.map { item ->
            DailyForecast(
                date = item.dt ?: 0L,
                tempDay = item.main?.temp ?: 0.0,
                tempMin = item.main?.tempMin ?: 0.0,
                tempMax = item.main?.tempMax ?: 0.0,
                weatherDescription = item.weather?.firstOrNull()?.description.orEmpty(),
                icon = item.weather?.firstOrNull()?.icon.orEmpty()
            )
        } ?: emptyList()
        
        return Forecast(city, list)
    }
}