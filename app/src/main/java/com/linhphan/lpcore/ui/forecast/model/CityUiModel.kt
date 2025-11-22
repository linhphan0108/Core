package com.linhphan.lpcore.ui.forecast.model

data class CityUiModel(
    val name: String,
    val coordinate: CoordinateUiModel
) {
    override fun toString(): String = name
}