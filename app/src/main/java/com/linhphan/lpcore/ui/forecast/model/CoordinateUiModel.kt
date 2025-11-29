package com.linhphan.lpcore.ui.forecast.model

data class CoordinateUiModel(
    val lat: Double,
    val lon: Double,
    val timezone: String = DEFAULT_TIMEZONE,
)

const val DEFAULT_TIMEZONE = "Asia/Bangkok"