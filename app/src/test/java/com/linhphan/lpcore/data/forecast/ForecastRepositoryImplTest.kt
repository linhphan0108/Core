package com.linhphan.lpcore.data.forecast

import com.linhphan.lpcore.data.forecast.local.ForecastLocalDataSource
import com.linhphan.lpcore.data.forecast.remote.ForecastRemoteDataSource
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.model.WeatherCondition
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
        val hourlyForecasts = HourlyForecasts(CityInfo("City", "Country"), emptyList())
        every { localDataSource.getForecast() } returns flowOf(hourlyForecasts)

        // When
        val result = repository.getForecast(0.0, 0.0).first()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(hourlyForecasts, (result as Result.Success).data)
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
        val hourlyForecasts = HourlyForecasts(CityInfo("City", "Country"), emptyList())
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2024-01-01"
        val endDate = "2024-01-02"
        
        coEvery { remoteDataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate) } returns Result.Success(hourlyForecasts)

        // When
        val result = repository.getHourlyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(hourlyForecasts, (result as Result.Success).data)
        coVerify { remoteDataSource.getHourlyForecast(lat, lon, timezone, startDate, endDate) }
    }

    @Test
    fun `getDailyForecast delegates to remoteDataSource`() = runTest(testDispatcher) {
        // Given
        val dailyForecasts = DailyForecasts(CityInfo("City", "Country"), emptyList())
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2024-01-01"
        val endDate = "2024-01-10"

        coEvery { remoteDataSource.getDailyForecast(lat, lon, timezone, startDate, endDate) } returns Result.Success(dailyForecasts)

        // When
        val result = repository.getDailyForecast(lat, lon, timezone, startDate, endDate)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(dailyForecasts, (result as Result.Success).data)
        coVerify { remoteDataSource.getDailyForecast(lat, lon, timezone, startDate, endDate) }
    }

    @Test
    fun `getCurrentForecast delegates to remoteDataSource`() = runTest(testDispatcher) {
        // Given
        val currentForecast = CurrentForecast(
            CityInfo("City", "Country"),
            HourlyForecast(0L, 0.0, 0.0, 0.0, 0.0, WeatherCondition.CLEAR_SKY, "", 0)
        )
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"

        coEvery { remoteDataSource.getCurrentForecast(lat, lon, timezone) } returns Result.Success(currentForecast)

        // When
        val result = repository.getCurrentForecast(lat, lon, timezone)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(currentForecast, (result as Result.Success).data)
        coVerify { remoteDataSource.getCurrentForecast(lat, lon, timezone) }
    }
}
