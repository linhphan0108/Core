package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSource
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSource
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecasts
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ForecastRepositoryImplTest {

    @MockK
    lateinit var remoteDataSource: ForecastRemoteDataSource

    @MockK
    lateinit var localDataSource: ForecastLocalDataSource

    private lateinit var repository: ForecastRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = ForecastRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
    }

    @Test
    fun `getForecast returns success when local data exists`() = runTest(testDispatcher) {
        // Given
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        every { localDataSource.getForecast() } returns flowOf(forecasts)

        // When
        val result = repository.getForecast(0.0, 0.0).first()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(forecasts, (result as Result.Success).data)
    }

    @Test
    fun `getForecast returns error when local data is null`() = runTest(testDispatcher) {
        // Given
        every { localDataSource.getForecast() } returns flowOf(null)

        // When
        val result = repository.getForecast(0.0, 0.0).first()

        // Then
        assertTrue(result is Result.Error)
        assertEquals("No local data found", (result as Result.Error).exception.message)
    }

    @Test
    fun `getHourlyForecast delegates to remoteDataSource`() = runTest(testDispatcher) {
        // Given
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2024-01-01"
        val endDate = "2024-01-02"
        
        coEvery { remoteDataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate) } returns Result.Success(forecasts)

        // When
        val result = repository.getHourlyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(forecasts, (result as Result.Success).data)
        coVerify { remoteDataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate) }
    }
}
