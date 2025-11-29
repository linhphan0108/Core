package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.HourlyDto
import com.linhphan.lpcore.data.forecast.model.OpenMeteoResponseDto
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
        
        val response = OpenMeteoResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyDto(
                time = listOf(timeStr),
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
        assertEquals(1, result.forecasts.size)
        
        val forecast = result.forecasts.first()
        assertEquals(expectedDate, forecast.date)
        assertEquals(25.0, forecast.tempDay, 0.0)
        assertEquals(25.0, forecast.tempMin, 0.0)
        assertEquals(25.0, forecast.tempMax, 0.0)
        assertEquals("Clear sky", forecast.weatherDescription)
        assertEquals("", forecast.icon) // Icon mapping is not yet implemented
    }
    
    @Test
    fun `mapToDomain handles empty lists`() {
         // Given
        val response = OpenMeteoResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyDto(
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
        assertEquals(0, result.forecasts.size)
    }
    
    @Test
    fun `mapToDomain handles null hourly data`() {
         // Given
        val response = OpenMeteoResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = null
        )

        // When
        val result = mapper.mapToDomain(response)
        
        // Then
        assertEquals(0, result.forecasts.size)
    }

    @Test
    fun `mapToDomain handles null lists inside hourly`() {
        // Given
        val response = OpenMeteoResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyDto(
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
        assertEquals(0, result.forecasts.size)
    }

    @Test
    fun `mapToDomain handles mismatched list sizes`() {
        // Given
        // Time has 2 items, but temp has 1, weatherCode has 2. Should only map 1 item.
        val response = OpenMeteoResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyDto(
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
        assertEquals(1, result.forecasts.size)
    }
}
