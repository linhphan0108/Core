package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.data.forecast.model.CurrentForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.DailyForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
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
        
        val responseDto = HourlyForecastResponseDto(10.0, 20.0, null)
        val domainHourlyForecasts = HourlyForecasts(CityInfo("Unknown Location", "Unknown"), emptyList())

        coEvery { 
            apiService.getHourlyForecast(lat, lon, timezone = timezone, startDate = startDate, endDate = endDate) 
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } returns domainHourlyForecasts

        // When
        val result = dataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(domainHourlyForecasts, (result as Result.Success).data)
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
        val responseDto = HourlyForecastResponseDto(10.0, 20.0, null)
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

    @Test
    fun `getDailyForecast success returns Result Success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val startDate = "2024-01-01"
        val endDate = "2024-01-10"
        
        val responseDto = DailyForecastResponseDto(10.0, 20.0, null)
        val domainDailyForecasts = DailyForecasts(CityInfo("Unknown Location", "Unknown"), emptyList())

        coEvery { 
            apiService.getDailyForecast(lat, lon, timezone = timezone, startDate = startDate, endDate = endDate) 
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } returns domainDailyForecasts

        // When
        val result = dataSource.getDailyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(domainDailyForecasts, (result as Result.Success).data)
    }

    @Test
    fun `getDailyForecast api failure returns Result Error`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val exception = RuntimeException("Network error")

        coEvery { 
            apiService.getDailyForecast(lat, lon, timezone = timezone)
        } throws exception

        // When
        val result = dataSource.getDailyForecast(lat, lon, timezone, null, null)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `getCurrentForecast success returns Result Success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        
        val responseDto = CurrentForecastResponseDto(10.0, 20.0, null)
        val domainCurrentForecast = CurrentForecast(
            CityInfo("Unknown Location", "Unknown"), 
            HourlyForecast(0L, 0.0, 0.0, 0.0, 0.0, WeatherCondition.CLEAR_SKY, "", 0)
        )

        coEvery { 
            apiService.getCurrentForecast(lat, lon, timezone = timezone) 
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } returns domainCurrentForecast

        // When
        val result = dataSource.getCurrentForecast(lat, lon, timezone)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(domainCurrentForecast, (result as Result.Success).data)
    }

    @Test
    fun `getCurrentForecast returns Result Error when mapping returns null`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        
        val responseDto = CurrentForecastResponseDto(10.0, 20.0, null)

        coEvery { 
            apiService.getCurrentForecast(lat, lon, timezone = timezone) 
        } returns responseDto
        every { mapper.mapToDomain(responseDto) } returns null

        // When
        val result = dataSource.getCurrentForecast(lat, lon, timezone)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("No data found", (result as Result.Error).exception.message)
    }
}
