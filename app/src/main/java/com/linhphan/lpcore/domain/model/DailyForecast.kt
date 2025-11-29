package com.linhphan.lpcore.domain.model

data class DailyForecasts(
    val city: CityInfo,
    val dailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    val date: Long,
    val weatherCondition: WeatherCondition,
    val tempMax: Double,
    val tempMin: Double,
    val apparentTempMax: Double,
    val apparentTempMin: Double,
    val sunrise: Long,
    val sunset: Long,
    val uvIndexMax: Double,
    val precipitationSum: Double,
    val precipitationProbabilityMax: Int,
    val windSpeedMax: Double
)
