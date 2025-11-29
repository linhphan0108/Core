package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.OpenMeteoResponseDto
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ForecastMapper @Inject constructor() {
    fun mapToDomain(response: OpenMeteoResponseDto): Forecasts {
        val city = CityInfo(
            name = "Unknown Location", // Open-Meteo doesn't return city name
            country = "Unknown"
        )

        val hourly = response.hourly
        val times = hourly?.time ?: emptyList()
        val temperatures = hourly?.temperature2m ?: emptyList()
        val weatherCodes = hourly?.weatherCode ?: emptyList()
        val precipitationProbabilities = hourly?.precipitationProbability ?: emptyList()

        // Use SimpleDateFormat for API < 26 compatibility
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val forecastList = times.mapIndexedNotNull { index, timeStr ->
            if (index < temperatures.size && index < weatherCodes.size) {
                // Parse time string "2024-01-01T00:00" to Unix timestamp
                val dateLong = try {
                    val date = dateFormat.parse(timeStr)
                    (date?.time ?: 0L) / 1000 // Convert milliseconds to seconds
                } catch (e: Exception) {
                    0L
                }

                val temp = temperatures[index]
                val code = weatherCodes[index]
                val precipitationProbability = if (index < precipitationProbabilities.size) precipitationProbabilities[index] else 0
                
                Forecast(
                    date = dateLong,
                    tempDay = temp,
                    tempMin = temp, // Hourly data doesn't have min/max range, just instantaneous
                    tempMax = temp,
                    weatherCondition = WeatherCondition.fromCode(code),
                    icon = "", // We need a way to map code to icon, or leave blank for now
                    precipitationProbability = precipitationProbability
                )
            } else {
                null
            }
        }
        
        return Forecasts(city, forecastList)
    }
}