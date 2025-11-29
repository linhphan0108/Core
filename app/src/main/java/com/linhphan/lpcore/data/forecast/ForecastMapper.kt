package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.CurrentForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.DailyForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.DailyForecast
import com.linhphan.lpcore.domain.model.DailyForecasts
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
        val apparentTemperatures = hourly?.apparentTemperature ?: emptyList()

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
                val apparentTemperature = if (index < apparentTemperatures.size) apparentTemperatures[index] else temp
                
                HourlyForecast(
                    date = dateLong,
                    tempDay = temp,
                    tempMin = temp, // Hourly data doesn't have min/max range, just instantaneous
                    tempMax = temp,
                    apparentTemperature = apparentTemperature,
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
            apparentTemperature = current.apparentTemperature ?: 0.0,
            weatherCondition = WeatherCondition.fromCode(current.weatherCode ?: 0),
            icon = "",
            precipitationProbability = 0 // Current weather doesn't give probability usually
        )
        
        return CurrentForecast(city, forecast)
    }

    fun mapToDomain(response: DailyForecastResponseDto): DailyForecasts {
        val city = CityInfo(
            name = "Unknown Location",
            country = "Unknown"
        )

        val daily = response.daily
        val times = daily?.time ?: emptyList()
        val weatherCodes = daily?.weatherCode ?: emptyList()
        val tempMaxs = daily?.temperature2mMax ?: emptyList()
        val tempMins = daily?.temperature2mMin ?: emptyList()
        val apparentTempMaxs = daily?.apparentTemperatureMax ?: emptyList()
        val apparentTempMins = daily?.apparentTemperatureMin ?: emptyList()
        val sunrises = daily?.sunrise ?: emptyList()
        val sunsets = daily?.sunset ?: emptyList()
        val uvIndexMaxs = daily?.uvIndexMax ?: emptyList()
        val precipitationSums = daily?.precipitationSum ?: emptyList()
        val precipitationProbabilityMaxs = daily?.precipitationProbabilityMax ?: emptyList()
        val windSpeedMaxs = daily?.windSpeed10mMax ?: emptyList()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")

        val dailyForecastList = times.mapIndexedNotNull { index, timeStr ->
            if (index < weatherCodes.size && index < tempMaxs.size && index < tempMins.size) {
                val dateLong = try {
                    val date = dateFormat.parse(timeStr)
                    (date?.time ?: 0L) / 1000
                } catch (e: Exception) {
                    0L
                }
                
                val sunriseLong = try {
                    val date = if (index < sunrises.size) dateTimeFormat.parse(sunrises[index]) else null
                    (date?.time ?: 0L) / 1000
                } catch (e: Exception) {
                    0L
                }

                val sunsetLong = try {
                    val date = if (index < sunsets.size) dateTimeFormat.parse(sunsets[index]) else null
                    (date?.time ?: 0L) / 1000
                } catch (e: Exception) {
                    0L
                }

                DailyForecast(
                    date = dateLong,
                    weatherCondition = WeatherCondition.fromCode(weatherCodes[index]),
                    tempMax = tempMaxs[index],
                    tempMin = tempMins[index],
                    apparentTempMax = if (index < apparentTempMaxs.size) apparentTempMaxs[index] else 0.0,
                    apparentTempMin = if (index < apparentTempMins.size) apparentTempMins[index] else 0.0,
                    sunrise = sunriseLong,
                    sunset = sunsetLong,
                    uvIndexMax = if (index < uvIndexMaxs.size) uvIndexMaxs[index] else 0.0,
                    precipitationSum = if (index < precipitationSums.size) precipitationSums[index] else 0.0,
                    precipitationProbabilityMax = if (index < precipitationProbabilityMaxs.size) precipitationProbabilityMaxs[index] else 0,
                    windSpeedMax = if (index < windSpeedMaxs.size) windSpeedMaxs[index] else 0.0
                )
            } else {
                null
            }
        }

        return DailyForecasts(city, dailyForecastList)
    }
}
