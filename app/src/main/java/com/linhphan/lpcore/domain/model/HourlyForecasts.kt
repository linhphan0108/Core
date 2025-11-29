package com.linhphan.lpcore.domain.model

data class HourlyForecasts(
    val city: CityInfo,
    val hourlyForecasts: List<HourlyForecast>
)

data class CityInfo(
    val name: String,
    val country: String
)

data class HourlyForecast(
    val date: Long,
    val tempDay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherCondition: WeatherCondition,
    val icon: String,
    val precipitationProbability: Int
)