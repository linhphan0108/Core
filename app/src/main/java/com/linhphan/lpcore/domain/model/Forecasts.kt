package com.linhphan.lpcore.domain.model

data class Forecasts(
    val city: CityInfo,
    val forecasts: List<Forecast>
)

data class CityInfo(
    val name: String,
    val country: String
)

data class Forecast(
    val date: Long,
    val tempDay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherCondition: WeatherCondition,
    val icon: String,
    val precipitationProbability: Int
)