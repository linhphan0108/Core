package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.OpenMeteoResponseDto
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

        val forecastList = times.mapIndexedNotNull { index, timeStr ->
            if (index < temperatures.size && index < weatherCodes.size) {
                // Parse time string "2024-01-01T00:00" to Unix timestamp if needed or just keep date
                // The domain model expects 'date: Long' (Unix timestamp?)
                // Previous implementation mapped 'dt' (Unix UTC) to 'date'
                
                // OpenMeteo returns ISO8601 "yyyy-MM-dd'T'HH:mm"
                val dateLong = try {
                    // Assuming UTC or converting simple ISO to Epoch Second
                    // OpenMeteo defaults to GMT/UTC if not specified
                    java.time.LocalDateTime.parse(timeStr).atZone(ZoneId.of("UTC")).toEpochSecond()
                } catch (e: Exception) {
                    0L
                }

                val temp = temperatures[index]
                val code = weatherCodes[index]
                
                Forecast(
                    date = dateLong,
                    tempDay = temp,
                    tempMin = temp, // Hourly data doesn't have min/max range, just instantaneous
                    tempMax = temp,
                    weatherDescription = getWeatherDescription(code),
                    icon = "" // We need a way to map code to icon, or leave blank for now
                )
            } else {
                null
            }
        }
        
        return Forecasts(city, forecastList)
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
            45, 48 -> "Fog and depositing rime fog"
            51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
            56, 57 -> "Freezing Drizzle: Light and dense intensity"
            61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
            66, 67 -> "Freezing Rain: Light and heavy intensity"
            71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
            85, 86 -> "Snow showers slight and heavy"
            95 -> "Thunderstorm: Slight or moderate"
            96, 99 -> "Thunderstorm with slight and heavy hail"
            else -> "Unknown weather code: $code"
        }
    }
}
