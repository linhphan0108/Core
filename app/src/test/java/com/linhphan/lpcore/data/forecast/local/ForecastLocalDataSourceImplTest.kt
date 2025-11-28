package com.linhphan.lpcore.data.forecast.local

import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.model.Forecasts
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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
    fun `getForecast returns null when dao returns empty list`() = runTest {
        // Given
        every { forecastDao.getAllForecasts() } returns flowOf(emptyList())

        // When
        val result = dataSource.getForecast().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `getForecast maps entity to domain when dao returns data`() = runTest {
        // Given
        val entity = ForecastEntity(
            id = 1,
            cityName = "City",
            country = "Country",
            date = 100L,
            tempDay = 20.0,
            tempMin = 15.0,
            tempMax = 25.0,
            weatherDescription = "Cloudy",
            icon = "01d"
        )
        every { forecastDao.getAllForecasts() } returns flowOf(listOf(entity))

        // When
        val result = dataSource.getForecast().first()

        // Then
        assertNotNull(result)
        assertEquals("City", result?.city?.name)
        assertEquals("Country", result?.city?.country)
        assertEquals(1, result?.forecasts?.size)
        
        val forecast = result?.forecasts?.first()
        assertEquals(100L, forecast?.date)
        assertEquals("Cloudy", forecast?.weatherDescription)
    }

    @Test
    fun `saveForecast inserts forecasts using dao`() = runTest {
        // Given
        val forecasts = Forecasts(
            city = CityInfo("City", "Country"),
            forecasts = listOf(
                Forecast(
                    date = 100L,
                    tempDay = 20.0,
                    tempMin = 15.0,
                    tempMax = 25.0,
                    weatherDescription = "Cloudy",
                    icon = "01d"
                )
            )
        )
        
        coEvery { forecastDao.insertForecastsWithLimit(any(), any()) } returns Unit

        // When
        dataSource.saveForecast(forecasts)

        // Then
        coVerify { 
            forecastDao.insertForecastsWithLimit(
                match { entities ->
                    entities.size == 1 &&
                    entities[0].cityName == "City" &&
                    entities[0].weatherDescription == "Cloudy"
                },
                any()
            ) 
        }
    }
}