package com.linhphan.lpcore.ui.forecast.mapper

import android.content.Context
import com.linhphan.lpcore.R
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.ForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiModel
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

    fun mapToUiModel(cityUiModel: CityUiModel, domainModel: HourlyForecasts): HourlyForecastUiModel {
        return HourlyForecastUiModel(
            cityTitle = context.getString(
                R.string.city_country_format,
                cityUiModel.name,
                cityUiModel.country,
            ),
            items = domainModel.hourlyForecasts.map { mapItem(it) }
        )
    }

    private fun mapItem(domainItem: HourlyForecast): ForecastUiItem {
        val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        hourFormat.timeZone = TimeZone.getDefault()

        return ForecastUiItem(
            hour = hourFormat.format(Date(domainItem.date * 1000)),
            description = domainItem.weatherCondition.description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            },
            tempMax = context.getString(
                R.string.temperature_celsius,
                domainItem.tempMax.roundToInt()
            ),
            precipitationProbability = context.getString(
                R.string.precipitation_probability,
                domainItem.precipitationProbability
            ),
            iconRes = getWeatherIcon(domainItem.weatherCondition)
        )
    }

    private fun getWeatherIcon(condition: WeatherCondition): Int {
        return when (condition) {
            WeatherCondition.CLEAR_SKY -> R.drawable.ic_weather_clear_sky
            WeatherCondition.MAINLY_CLEAR,
            WeatherCondition.PARTLY_CLOUDY -> R.drawable.ic_weather_partly_cloudy
            WeatherCondition.OVERCAST,
            WeatherCondition.FOG,
            WeatherCondition.DEPOSITING_RIME_FOG -> R.drawable.ic_weather_fog
            WeatherCondition.DRIZZLE_LIGHT,
            WeatherCondition.DRIZZLE_MODERATE,
            WeatherCondition.DRIZZLE_DENSE,
            WeatherCondition.FREEZING_DRIZZLE_LIGHT,
            WeatherCondition.FREEZING_DRIZZLE_DENSE,
            WeatherCondition.RAIN_SLIGHT,
            WeatherCondition.RAIN_MODERATE,
            WeatherCondition.RAIN_HEAVY,
            WeatherCondition.FREEZING_RAIN_LIGHT,
            WeatherCondition.FREEZING_RAIN_HEAVY,
            WeatherCondition.RAIN_SHOWERS_SLIGHT,
            WeatherCondition.RAIN_SHOWERS_MODERATE,
            WeatherCondition.RAIN_SHOWERS_VIOLENT -> R.drawable.ic_weather_rain
            WeatherCondition.SNOW_FALL_SLIGHT,
            WeatherCondition.SNOW_FALL_MODERATE,
            WeatherCondition.SNOW_FALL_HEAVY,
            WeatherCondition.SNOW_GRAINS,
            WeatherCondition.SNOW_SHOWERS_SLIGHT,
            WeatherCondition.SNOW_SHOWERS_HEAVY -> R.drawable.ic_weather_snow
            WeatherCondition.THUNDERSTORM_SLIGHT_MODERATE,
            WeatherCondition.THUNDERSTORM_HAIL_SLIGHT,
            WeatherCondition.THUNDERSTORM_HAIL_HEAVY -> R.drawable.ic_weather_thunderstorm
            WeatherCondition.UNKNOWN -> R.drawable.ic_weather_clear_sky // Default or placeholder
        }
    }
}