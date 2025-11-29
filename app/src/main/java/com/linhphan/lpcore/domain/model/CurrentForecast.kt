package com.linhphan.lpcore.domain.model

data class CurrentForecast(
    val city: CityInfo,
    val current: HourlyForecast
)