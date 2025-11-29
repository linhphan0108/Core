package com.linhphan.lpcore.ui.forecast.mapper

import android.content.Context
import com.linhphan.lpcore.R
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.ForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.ForecastUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.math.roundToInt

class ForecastUiMapper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun mapToUiModel(cityUiModel: CityUiModel, domainModel: Forecasts): ForecastUiModel {
        return ForecastUiModel(
            cityTitle = context.getString(
                R.string.city_country_format,
                cityUiModel.name,
                cityUiModel.country,
            ),
            items = domainModel.forecasts.map { mapItem(it) }
        )
    }

    private fun mapItem(domainItem: Forecast): ForecastUiItem {
        val dateFormat = SimpleDateFormat("EEE, dd MMM HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        return ForecastUiItem(
            date = dateFormat.format(Date(domainItem.date * 1000)),
            description = domainItem.weatherDescription.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            },
            tempMax = context.getString(
                R.string.temperature_celsius,
                domainItem.tempMax.roundToInt()
            ),
            tempMin = context.getString(
                R.string.temperature_celsius,
                domainItem.tempMin.roundToInt()
            )
        )
    }
}