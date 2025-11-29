package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.CurrentForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ForecastMapper @Inject constructor() {
    fun mapToDomain(response: HourlyForecastResponseDto): HourlyForecasts {
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

        val hourlyForecastList = times.mapIndexedNotNull { index, timeStr ->
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
                
                HourlyForecast(
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
        
        return HourlyForecasts(city, hourlyForecastList)
    }

    fun mapToDomain(response: CurrentForecastResponseDto): CurrentForecast? {
        val current = response.currentForecasts ?: return null
        val city = CityInfo(
            name = "Unknown Location", // Open-Meteo doesn't return city name
            country = "Unknown"
        )
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        
        val dateLong = try {
            val date = dateFormat.parse(current.time ?: "")
            (date?.time ?: 0L) / 1000
        } catch (e: Exception) {
            0L
        }
        
        val forecast = HourlyForecast(
            date = dateLong,
            tempDay = current.temperature2m ?: 0.0,
            tempMin = current.temperature2m ?: 0.0,
            tempMax = current.temperature2m ?: 0.0,
            weatherCondition = WeatherCondition.fromCode(current.weatherCode ?: 0),
            icon = "",
            precipitationProbability = 0 // Current weather doesn't give probability usually
        )
        
        return CurrentForecast(city, forecast)
    }
}