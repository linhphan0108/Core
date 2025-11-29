package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.data.forecast.model.OpenMeteoResponseDto
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecasts
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ForecastRemoteDataSourceImplTest {

    @MockK
    lateinit var apiService: ForecastApiService

    @MockK
    lateinit var mapper: ForecastMapper

    private lateinit var dataSource: ForecastRemoteDataSourceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dataSource = ForecastRemoteDataSourceImpl(apiService, mapper)
    }

    @Test
    fun `getHourlyForecast returns success when api succeeds`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2024-01-01"
        val endDate = "2024-01-02"
        
        val response = OpenMeteoResponseDto(lat, lon, null) 
        val expectedForecasts = Forecasts(CityInfo("Unknown Location", "Unknown"), emptyList())

        // Mock apiService.getHourlyForecast. 
        // The actual call uses default 'hourly' param, so we match any string for it, and any for timezone/dates.
        coEvery { apiService.getHourlyForecast(lat, lon, any(), any(), any(), any()) } returns response
        every { mapper.mapToDomain(response) } returns expectedForecasts

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedForecasts, (result as Result.Success).data)
    }

    @Test
    fun `getHourlyForecast returns error when api fails`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val exception = RuntimeException("Network Error")

        // Match any arguments for simplicity in error case
        coEvery { apiService.getHourlyForecast(any(), any(), any(), any(), any(), any()) } throws exception

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, null, null)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}
