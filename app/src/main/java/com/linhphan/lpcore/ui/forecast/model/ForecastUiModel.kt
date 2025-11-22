package com.linhphan.lpcore.ui.forecast.model

data class ForecastUiModel(
    val cityTitle: String = "",
    val items: List<ForecastUiItem> = emptyList(),
    val errorMessage: String? = null
)

data class ForecastUiItem(
    val date: String,
    val description: String,
    val tempMax: String,
    val tempMin: String
)