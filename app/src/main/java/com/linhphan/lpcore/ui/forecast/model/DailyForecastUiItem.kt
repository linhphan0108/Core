package com.linhphan.lpcore.ui.forecast.model

import androidx.annotation.DrawableRes

data class DailyForecastUiItem(
    val day: String,
    val date: String,
    val description: String,
    val tempMax: String,
    val tempMin: String,
    val precipitationProbability: String,
    @DrawableRes val iconRes: Int,
    var isSelected: Boolean = false
)
