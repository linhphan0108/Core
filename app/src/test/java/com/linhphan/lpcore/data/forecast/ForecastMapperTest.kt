package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.HourlyForecastDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import com.linhphan.lpcore.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

class ForecastMapperTest {

    private val mapper = ForecastMapper()

    @Test
    fun `mapToDomain maps correctly`() {
        // Given
        val timeStr = "2024-01-01T12:00"
        val expectedDate = java.time.LocalDateTime.parse(timeStr).atZone(ZoneId.of("UTC")).toEpochSecond()
        
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyForecastDto(
                time = listOf(timeStr),
                temperature2m = listOf(25.0),
                relativeHumidity2m = null,
                dewPoint2m = null,
                apparentTemperature = listOf(25.0),
                precipitationProbability = listOf(10),
                precipitation = null,
                rain = null,
                showers = null,
                snowfall = null,
                snowDepth = null,
                weatherCode = listOf(0),
                pressureMsl = null,
                surfacePressure = null,
                cloudCover = null,
                cloudCoverLow = null,
                cloudCoverMid = null,
                cloudCoverHigh = null,
                visibility = null,
                evapotranspiration = null,
                et0FaoEvapotranspiration = null,
                vapourPressureDeficit = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals("Unknown Location", result.city.name)
        assertEquals(1, result.hourlyForecasts.size)
        
        val forecast = result.hourlyForecasts.first()
        assertEquals(expectedDate, forecast.date)
        assertEquals(25.0, forecast.tempDay, 0.0)
        assertEquals(25.0, forecast.tempMin, 0.0)
        assertEquals(25.0, forecast.tempMax, 0.0)
        assertEquals(WeatherCondition.CLEAR_SKY, forecast.weatherCondition)
        assertEquals(10, forecast.precipitationProbability)
        assertEquals(25.0, forecast.apparentTemperature, 0.0)
        assertEquals("", forecast.icon) // Icon mapping is not yet implemented
    }
    
    @Test
    fun `mapToDomain handles empty lists`() {
         // Given
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyForecastDto(
                time = emptyList(),
                temperature2m = emptyList(),
                relativeHumidity2m = null,
                dewPoint2m = null,
                apparentTemperature = null,
                precipitationProbability = null,
                precipitation = null,
                rain = null,
                showers = null,
                snowfall = null,
                snowDepth = null,
                weatherCode = emptyList(),
                pressureMsl = null,
                surfacePressure = null,
                cloudCover = null,
                cloudCoverLow = null,
                cloudCoverMid = null,
                cloudCoverHigh = null,
                visibility = null,
                evapotranspiration = null,
                et0FaoEvapotranspiration = null,
                vapourPressureDeficit = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)
        
        // Then
        assertEquals(0, result.hourlyForecasts.size)
    }
    
    @Test
    fun `mapToDomain handles null hourly data`() {
         // Given
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = null
        )

        // When
        val result = mapper.mapToDomain(response)
        
        // Then
        assertEquals(0, result.hourlyForecasts.size)
    }

    @Test
    fun `mapToDomain handles null lists inside hourly`() {
        // Given
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyForecastDto(
                time = null,
                temperature2m = null,
                relativeHumidity2m = null,
                dewPoint2m = null,
                apparentTemperature = null,
                precipitationProbability = null,
                precipitation = null,
                rain = null,
                showers = null,
                snowfall = null,
                snowDepth = null,
                weatherCode = null,
                pressureMsl = null,
                surfacePressure = null,
                cloudCover = null,
                cloudCoverLow = null,
                cloudCoverMid = null,
                cloudCoverHigh = null,
                visibility = null,
                evapotranspiration = null,
                et0FaoEvapotranspiration = null,
                vapourPressureDeficit = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(0, result.hourlyForecasts.size)
    }

    @Test
    fun `mapToDomain handles mismatched list sizes`() {
        // Given
        // Time has 2 items, but temp has 1, weatherCode has 2. Should only map 1 item.
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyForecastDto(
                time = listOf("2024-01-01T12:00", "2024-01-01T13:00"),
                temperature2m = listOf(25.0),
                relativeHumidity2m = null,
                dewPoint2m = null,
                apparentTemperature = null,
                precipitationProbability = null,
                precipitation = null,
                rain = null,
                showers = null,
                snowfall = null,
                snowDepth = null,
                weatherCode = listOf(0, 1),
                pressureMsl = null,
                surfacePressure = null,
                cloudCover = null,
                cloudCoverLow = null,
                cloudCoverMid = null,
                cloudCoverHigh = null,
                visibility = null,
                evapotranspiration = null,
                et0FaoEvapotranspiration = null,
                vapourPressureDeficit = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(1, result.hourlyForecasts.size)
    }
}