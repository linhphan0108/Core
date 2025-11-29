package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.CurrentForecastDto
import com.linhphan.lpcore.data.forecast.model.CurrentForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.DailyForecastDto
import com.linhphan.lpcore.data.forecast.model.DailyForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import com.linhphan.lpcore.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.ZoneId

class ForecastMapperTest {

    private val mapper = ForecastMapper()

    @Test
    fun `mapToDomain maps hourly correctly`() {
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
        assertEquals("", forecast.icon)
    }
    
    @Test
    fun `mapToDomain handles empty hourly lists`() {
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
    fun `mapToDomain handles mismatched hourly list sizes`() {
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

    @Test
    fun `mapToDomain handles invalid date format in hourly`() {
        // Given
        val response = HourlyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            hourly = HourlyForecastDto(
                time = listOf("invalid-date"),
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
        assertEquals(1, result.hourlyForecasts.size)
        assertEquals(0L, result.hourlyForecasts.first().date)
    }

    @Test
    fun `mapToDomain maps daily correctly`() {
        // Given
        val timeStr = "2024-01-01"
        val expectedDate = java.time.LocalDate.parse(timeStr).atStartOfDay(ZoneId.of("UTC")).toEpochSecond()

        val response = DailyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            daily = DailyForecastDto(
                time = listOf(timeStr),
                weatherCode = listOf(0),
                temperature2mMax = listOf(30.0),
                temperature2mMin = listOf(20.0),
                apparentTemperatureMax = listOf(32.0),
                apparentTemperatureMin = listOf(22.0),
                sunrise = listOf("2024-01-01T06:00"),
                sunset = listOf("2024-01-01T18:00"),
                uvIndexMax = listOf(5.0),
                precipitationSum = listOf(0.0),
                precipitationProbabilityMax = listOf(0),
                windSpeed10mMax = listOf(10.0)
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals("Unknown Location", result.city.name)
        assertEquals(1, result.dailyForecasts.size)

        val forecast = result.dailyForecasts.first()
        assertEquals(expectedDate, forecast.date)
        assertEquals(30.0, forecast.tempMax, 0.0)
        assertEquals(20.0, forecast.tempMin, 0.0)
        assertEquals(WeatherCondition.CLEAR_SKY, forecast.weatherCondition)
        assertEquals(0, forecast.precipitationProbabilityMax)
    }

    @Test
    fun `mapToDomain handles empty daily lists`() {
        // Given
        val response = DailyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            daily = DailyForecastDto(
                time = emptyList(),
                weatherCode = emptyList(),
                temperature2mMax = emptyList(),
                temperature2mMin = emptyList(),
                apparentTemperatureMax = emptyList(),
                apparentTemperatureMin = emptyList(),
                sunrise = emptyList(),
                sunset = emptyList(),
                uvIndexMax = emptyList(),
                precipitationSum = emptyList(),
                precipitationProbabilityMax = emptyList(),
                windSpeed10mMax = emptyList()
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(0, result.dailyForecasts.size)
    }

    @Test
    fun `mapToDomain handles mismatched daily list sizes`() {
        // Given
        val response = DailyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            daily = DailyForecastDto(
                time = listOf("2024-01-01", "2024-01-02"),
                weatherCode = listOf(0), // Only 1 item
                temperature2mMax = listOf(30.0, 31.0),
                temperature2mMin = listOf(20.0, 21.0),
                apparentTemperatureMax = null,
                apparentTemperatureMin = null,
                sunrise = null,
                sunset = null,
                uvIndexMax = null,
                precipitationSum = null,
                precipitationProbabilityMax = null,
                windSpeed10mMax = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(1, result.dailyForecasts.size)
    }

    @Test
    fun `mapToDomain handles invalid date format in daily`() {
        // Given
        val response = DailyForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            daily = DailyForecastDto(
                time = listOf("invalid-date"),
                weatherCode = listOf(0),
                temperature2mMax = listOf(30.0),
                temperature2mMin = listOf(20.0),
                apparentTemperatureMax = null,
                apparentTemperatureMin = null,
                sunrise = null,
                sunset = null,
                uvIndexMax = null,
                precipitationSum = null,
                precipitationProbabilityMax = null,
                windSpeed10mMax = null
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(1, result.dailyForecasts.size)
        assertEquals(0L, result.dailyForecasts.first().date)
    }
    
    @Test
    fun `mapToDomain maps current forecast correctly`() {
        // Given
        val timeStr = "2024-01-01T12:00"
        val expectedDate = java.time.LocalDateTime.parse(timeStr).atZone(ZoneId.of("UTC")).toEpochSecond()

        val response = CurrentForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            currentForecasts = CurrentForecastDto(
                time = timeStr,
                temperature2m = 25.0,
                relativeHumidity2m = 50,
                apparentTemperature = 27.0,
                isDay = 1,
                precipitation = 0.0,
                rain = 0.0,
                showers = 0.0,
                snowfall = 0.0,
                weatherCode = 0,
                cloudCover = 0,
                pressureMsl = 1013.0,
                surfacePressure = 1000.0,
                windSpeed10m = 10.0,
                windDirection10m = 180.0,
                windGusts10m = 15.0
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals("Unknown Location", result?.city?.name)
        assertEquals(expectedDate, result?.current?.date)
        assertEquals(25.0, result?.current?.tempDay ?: 0.0, 0.0)
        assertEquals(WeatherCondition.CLEAR_SKY, result?.current?.weatherCondition)
    }

    @Test
    fun `mapToDomain returns null when current forecast is null`() {
        // Given
        val response = CurrentForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            currentForecasts = null
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertNull(result)
    }

    @Test
    fun `mapToDomain handles invalid date format in current forecast`() {
        // Given
        val response = CurrentForecastResponseDto(
            latitude = 52.52,
            longitude = 13.41,
            currentForecasts = CurrentForecastDto(
                time = "invalid-date",
                temperature2m = 25.0,
                relativeHumidity2m = 50,
                apparentTemperature = 27.0,
                isDay = 1,
                precipitation = 0.0,
                rain = 0.0,
                showers = 0.0,
                snowfall = 0.0,
                weatherCode = 0,
                cloudCover = 0,
                pressureMsl = 1013.0,
                surfacePressure = 1000.0,
                windSpeed10m = 10.0,
                windDirection10m = 180.0,
                windGusts10m = 15.0
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals(0L, result?.current?.date)
    }
}
