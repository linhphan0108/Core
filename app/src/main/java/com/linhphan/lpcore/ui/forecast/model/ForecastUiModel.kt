package com.linhphan.lpcore.ui.forecast.model

data class ForecastUiModel(
    val cityTitle: String = "",
    val items: List<ForecastUiItem> = emptyList(),
    val errorMessage: String? = null
)

data class ForecastUiItem(
    val hour: String,
    val description: String,
    val tempMax: String,
    val precipitationProbability: String,
    val iconRes: Int? = null // Placeholder for icon resource if we mapped it, or we can use description for now
)