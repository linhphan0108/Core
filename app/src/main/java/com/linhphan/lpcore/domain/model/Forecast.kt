package com.linhphan.lpcore.domain.model

data class Forecast(
    val city: CityInfo,
    val dailyForecasts: List<DailyForecast>
)

data class CityInfo(
    val name: String,
    val country: String
)

data class DailyForecast(
    val date: Long,
    val tempDay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherDescription: String,
    val icon: String
)