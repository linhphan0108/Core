package com.linhphan.lpcore.ui.forecast.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinateUiModel(
    val lat: Double,
    val lon: Double,
    val timezone: String = DEFAULT_TIMEZONE,
) : Parcelable

const val DEFAULT_TIMEZONE = "Asia/Bangkok"