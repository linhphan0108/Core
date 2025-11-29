package com.linhphan.lpcore.ui.forecast.model

data class HourlyForecastUiItem(
    val hour: String,
    val description: String,
    val tempMax: String,
    val precipitationProbability: String,
    val iconRes: Int
)

data class CurrentForecastUiModel(
    val temp: String,
    val weatherCondition: String,
    val feelsLike: String,
    val highLow: String,
    val iconRes: Int
)