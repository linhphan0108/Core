package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.model.CityDto
import com.linhphan.lpcore.data.forecast.model.ForecastDto
import com.linhphan.lpcore.data.forecast.model.ForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.MainDto
import com.linhphan.lpcore.data.forecast.model.WeatherDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastMapperTest {

    private val mapper = ForecastMapper()

    @Test
    fun `mapToDomain maps correctly`() {
        // Given
        val response = ForecastResponseDto(
            cod = "200",
            message = 0,
            cnt = 1,
            city = CityDto(
                id = 1,
                name = "London",
                coord = null,
                country = "UK",
                population = 0,
                timezone = 0,
                sunrise = 0,
                sunset = 0
            ),
            list = listOf(
                ForecastDto(
                    dt = 1600000000L,
                    main = MainDto(
                        temp = 20.0,
                        tempMin = 15.0,
                        tempMax = 25.0,
                        feelsLike = 0.0,
                        pressure = 0,
                        seaLevel = 0,
                        grndLevel = 0,
                        humidity = 0,
                        tempKf = 0.0
                    ),
                    weather = listOf(
                        WeatherDto(
                            id = 800,
                            main = "Clear",
                            description = "Clear",
                            icon = "01d"
                        )
                    ),
                    clouds = null,
                    wind = null,
                    visibility = 0,
                    pop = 0.0,
                    rain = null,
                    snow = null,
                    sys = null,
                    dtTxt = ""
                )
            )
        )

        // When
        val result = mapper.mapToDomain(response)

        // Then
        assertEquals("London", result.city.name)
        assertEquals("UK", result.city.country)
        assertEquals(1, result.forecasts.size)
        
        val forecast = result.forecasts.first()
        assertEquals(1600000000L, forecast.date)
        assertEquals(20.0, forecast.tempDay, 0.0)
        assertEquals(15.0, forecast.tempMin, 0.0)
        assertEquals(25.0, forecast.tempMax, 0.0)
        assertEquals("Clear", forecast.weatherDescription)
        assertEquals("01d", forecast.icon)
    }
    
    @Test
    fun `mapToDomain handles empty list`() {
         // Given
        val response = ForecastResponseDto(
            cod = "200",
            message = 0,
            cnt = 0,
            city = CityDto(
                id = 1,
                name = "London",
                coord = null,
                country = "UK",
                population = 0,
                timezone = 0,
                sunrise = 0,
                sunset = 0
            ),
            list = emptyList()
        )

        // When
        val result = mapper.mapToDomain(response)
        
        // Then
        assertEquals(0, result.forecasts.size)
    }
    
    @Test
    fun `mapToDomain handles null list`() {
         // Given
        val response = ForecastResponseDto(
            cod = "200",
            message = 0,
            cnt = 0,
            city = CityDto(
                id = 1,
                name = "London",
                coord = null,
                country = "UK",
                population = 0,
                timezone = 0,
                sunrise = 0,
                sunset = 0
            ),
            list = null
        )

        // When
        val result = mapper.mapToDomain(response)
        
        // Then
        assertEquals(0, result.forecasts.size)
    }
}