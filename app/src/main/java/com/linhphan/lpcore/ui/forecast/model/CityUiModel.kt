package com.linhphan.lpcore.ui.forecast.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityUiModel(
    val name: String,
    val country: String,
    val coordinate: CoordinateUiModel
) : Parcelable {
    override fun toString(): String = "$name, $country"
}
