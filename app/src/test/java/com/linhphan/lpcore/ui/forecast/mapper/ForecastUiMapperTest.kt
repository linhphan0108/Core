package com.linhphan.lpcore.ui.forecast.mapper

import android.content.Context
import com.linhphan.lpcore.R
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
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
    fun `mapToUiModel maps correctly`() {
        // Given
        val cityUiModel = CityUiModel("Hanoi", "Vietnam", CoordinateUiModel(10.0, 20.0))
        val hourlyForecast = HourlyForecast(
            date = 1704085200L, // 2024-01-01 05:00:00 UTC
            tempDay = 25.0,
            tempMin = 20.0,
            tempMax = 30.0,
            weatherCondition = WeatherCondition.CLEAR_SKY,
            icon = "icon",
            precipitationProbability = 10
        )
        val hourlyForecasts = HourlyForecasts(
            CityInfo("Hanoi", "Vietnam"),
            listOf(hourlyForecast)
        )

        every { context.getString(R.string.city_country_format, "Hanoi", "Vietnam") } returns "Hanoi, Vietnam"
        every { context.getString(R.string.temperature_celsius, 30) } returns "30°C"
        every { context.getString(R.string.precipitation_probability, 10) } returns "10%"

        // When
        val result = mapper.mapToUiModel(cityUiModel, hourlyForecasts)

        // Then
        assertEquals("Hanoi, Vietnam", result.cityTitle)
        assertEquals(1, result.items.size)
        val item = result.items[0]
        
        // 1704085200L is Mon, 01 Jan 2024 05:00:00 UTC
        // The mapper uses "HH:mm"
        assertEquals("05:00", item.hour)
        
        assertEquals("Clear sky", item.description)
        assertEquals("30°C", item.tempMax)
        assertEquals("10%", item.precipitationProbability)
        assertEquals(R.drawable.ic_weather_clear_sky, item.iconRes)
    }
}