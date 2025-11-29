package com.linhphan.lpcore.ui.forecast.mapper

import android.content.Context
import com.linhphan.lpcore.R
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.DailyForecast
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class ForecastUiMapperTest {

    @MockK
    lateinit var context: Context

    private lateinit var mapper: ForecastUiMapper
    private lateinit var defaultTimeZone: TimeZone

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mapper = ForecastUiMapper(context)
        
        defaultTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
    
    @org.junit.After
    fun tearDown() {
        TimeZone.setDefault(defaultTimeZone)
    }

    @Test
    fun `mapToUiModel maps hourly forecast correctly`() {
        // Given
        val hourlyForecast = HourlyForecast(
            date = 1704085200L, // 2024-01-01 05:00:00 UTC
            tempDay = 25.0,
            tempMin = 20.0,
            tempMax = 30.0,
            apparentTemperature = 25.0,
            weatherCondition = WeatherCondition.CLEAR_SKY,
            icon = "icon",
            precipitationProbability = 10
        )
        val hourlyForecasts = HourlyForecasts(
            CityInfo("Hanoi", "Vietnam"),
            listOf(hourlyForecast)
        )

        every { context.getString(R.string.temperature_celsius, 30) } returns "30°C"
        every { context.getString(R.string.precipitation_probability, 10) } returns "10%"

        // When
        val result = mapper.mapToUiModel(hourlyForecasts)

        // Then
        assertEquals(1, result.size)
        val item = result[0]
        
        // 1704085200L is Mon, 01 Jan 2024 05:00:00 UTC
        assertEquals("05:00", item.hour)
        assertEquals("Clear sky", item.description)
        assertEquals("30°C", item.tempMax)
        assertEquals("10%", item.precipitationProbability)
        assertEquals(R.drawable.ic_weather_clear_sky, item.iconRes)
    }

    @Test
    fun `mapToUiModel maps current forecast correctly`() {
        // Given
        val hourlyForecast = HourlyForecast(
            date = 1704085200L,
            tempDay = 25.0,
            tempMin = 20.0,
            tempMax = 30.0,
            apparentTemperature = 27.0,
            weatherCondition = WeatherCondition.RAIN_HEAVY,
            icon = "icon",
            precipitationProbability = 0
        )
        val currentForecast = CurrentForecast(
            CityInfo("Hanoi", "Vietnam"),
            hourlyForecast
        )

        every { context.getString(R.string.temperature_celsius, 25) } returns "25°C"
        every { context.getString(R.string.temperature_celsius, 27) } returns "27°C"
        every { context.getString(R.string.temperature_celsius, 30) } returns "30°C"
        every { context.getString(R.string.temperature_celsius, 20) } returns "20°C"
        every { context.getString(R.string.feels_like, "27°C") } returns "Feels like 27°C"
        every { context.getString(R.string.high_low, "30°C", "20°C") } returns "H:30° L:20°"

        // When
        val result = mapper.mapToUiModel(currentForecast)

        // Then
        assertEquals("25°C", result.temp)
        assertEquals("Rain: Slight, moderate and heavy intensity", result.weatherCondition)
        assertEquals("Feels like 27°C", result.feelsLike)
        assertEquals("H:30° L:20°", result.highLow)
        assertEquals(R.drawable.ic_weather_rain, result.iconRes)
    }

    @Test
    fun `mapToUiModel maps daily forecast correctly`() {
        // Given
        val dailyForecast = DailyForecast(
            date = 1704085200L, // Mon, 01 Jan 2024
            weatherCondition = WeatherCondition.PARTLY_CLOUDY,
            tempMax = 30.0,
            tempMin = 20.0,
            apparentTempMax = 32.0,
            apparentTempMin = 22.0,
            sunrise = 0L,
            sunset = 0L,
            uvIndexMax = 5.0,
            precipitationSum = 0.0,
            precipitationProbabilityMax = 10,
            windSpeedMax = 10.0
        )
        val dailyForecasts = DailyForecasts(
            CityInfo("Hanoi", "Vietnam"),
            listOf(dailyForecast)
        )

        every { context.getString(R.string.temperature_celsius, 30) } returns "30°C"
        every { context.getString(R.string.temperature_celsius, 20) } returns "20°C"
        every { context.getString(R.string.precipitation_probability, 10) } returns "10%"

        // When
        val result = mapper.mapToUiModel(dailyForecasts)

        // Then
        assertEquals(1, result.size)
        val item = result[0]
        
        // Date: 1704085200L -> Mon, 01 Jan 2024
        // dayFormat "EEE" -> Mon
        // dateFormat "dd/MM" -> 01/01
        assertEquals("Mon", item.day)
        assertEquals("01/01", item.date)
        assertEquals("Mainly clear, partly cloudy, and overcast", item.description)
        assertEquals("30°C", item.tempMax)
        assertEquals("20°C", item.tempMin)
        assertEquals("10%", item.precipitationProbability)
        assertEquals(R.drawable.ic_weather_partly_cloudy, item.iconRes)
    }
}
