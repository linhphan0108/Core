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
    fun `getHourlyForecast success returns Result Success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val startDate = "2024-01-01"
        val endDate = "2024-01-02"
        
        val responseDto = OpenMeteoResponseDto(10.0, 20.0, null)
        val domainForecasts = Forecasts(CityInfo("Unknown Location", "Unknown"), emptyList())

        coEvery { 
            apiService.getHourlyForecast(lat, lon, timezone = timezone, startDate = startDate, endDate = endDate) 
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } returns domainForecasts

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(domainForecasts, (result as Result.Success).data)
    }

    @Test
    fun `getHourlyForecast api failure returns Result Error`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val exception = RuntimeException("Network error")

        coEvery { 
            apiService.getHourlyForecast(lat, lon, timezone = timezone)
        } throws exception

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, null, null)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
    
    @Test
    fun `getHourlyForecast mapper failure returns Result Error`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val responseDto = OpenMeteoResponseDto(10.0, 20.0, null)
        val exception = RuntimeException("Mapping error")

        coEvery { 
            apiService.getHourlyForecast(lat, lon, timezone = timezone)
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } throws exception

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, null, null)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}
