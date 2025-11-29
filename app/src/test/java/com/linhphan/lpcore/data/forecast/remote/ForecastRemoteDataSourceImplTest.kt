package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.data.forecast.model.ForecastResponseDto
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
    fun `getForecast returns success when api succeeds`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val response = ForecastResponseDto(null, null, null, null, null) // Mock response with correct number of arguments
        val expectedForecasts = Forecasts(CityInfo("City", "Country"), emptyList())

        coEvery { apiService.getThreeHourIntervalForecast(lat = lat, lon = lon, apiKey = any()) } returns response
        every { mapper.mapToDomain(response) } returns expectedForecasts

        // When
        val result = dataSource.getForecast(lat, lon)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedForecasts, (result as Result.Success).data)
    }

    @Test
    fun `getForecast returns error when api fails`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val exception = RuntimeException("Network Error")

        coEvery { apiService.getThreeHourIntervalForecast(lat = lat, lon = lon, apiKey = any()) } throws exception

        // When
        val result = dataSource.getForecast(lat, lon)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}