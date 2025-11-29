package com.linhphan.lpcore.data.forecast.local

import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ForecastLocalDataSourceImplTest {

    @MockK
    lateinit var forecastDao: ForecastDao

    private lateinit var dataSource: ForecastLocalDataSourceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dataSource = ForecastLocalDataSourceImpl(forecastDao)
    }

    @Test
    fun `getForecast returns null when database is empty`() = runTest {
        // Given
        every { forecastDao.getAllForecasts() } returns flowOf(emptyList())

        // When
        val result = dataSource.getForecast().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `getForecast returns mapped forecasts`() = runTest {
        // Given
        val entity = ForecastEntity(
            id = 1,
            cityName = "Hanoi",
            country = "Vietnam",
            date = 100L,
            tempDay = 25.0,
            tempMin = 20.0,
            tempMax = 30.0,
            weatherCode = 0,
            icon = "sunny",
            precipitationProbability = 10
        )
        every { forecastDao.getAllForecasts() } returns flowOf(listOf(entity))

        // When
        val result = dataSource.getForecast().first()

        // Then
        assertEquals("Hanoi", result?.city?.name)
        assertEquals(1, result?.forecasts?.size)
        
        val forecast = result?.forecasts?.first()
        assertEquals(100L, forecast?.date)
        assertEquals(WeatherCondition.CLEAR_SKY, forecast?.weatherCondition)
        assertEquals(10, forecast?.precipitationProbability)
    }

    @Test
    fun `saveForecast inserts entities`() = runTest {
        // Given
        val forecasts = Forecasts(
            CityInfo("Hanoi", "Vietnam"),
            listOf(
                Forecast(100L, 25.0, 20.0, 30.0, WeatherCondition.CLEAR_SKY, "sunny", 10)
            )
        )
        
        val slot = slot<List<ForecastEntity>>()
        coEvery { forecastDao.insertForecastsWithLimit(capture(slot), any()) } returns Unit

        // When
        dataSource.saveForecast(forecasts)

        // Then
        coVerify { forecastDao.insertForecastsWithLimit(any(), 40) }
        val entities = slot.captured
        assertEquals(1, entities.size)
        val entity = entities.first()
        assertEquals("Hanoi", entity.cityName)
        assertEquals(100L, entity.date)
        assertEquals(0, entity.weatherCode)
        assertEquals(10, entity.precipitationProbability)
    }
}