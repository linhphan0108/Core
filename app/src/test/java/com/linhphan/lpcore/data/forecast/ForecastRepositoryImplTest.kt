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
    fun `refreshForecast saves to local when remote success`() = runTest(testDispatcher) {
        // Given
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        coEvery { remoteDataSource.getHourlyForecast(any(), any()) } returns Result.Success(forecasts)
        coEvery { localDataSource.saveForecast(any()) } returns Unit

        // When
        val result = repository.refreshForecast(0.0, 0.0)

        // Then
        assertTrue(result is Result.Success)
        coVerify { localDataSource.saveForecast(forecasts) }
    }

    @Test
    fun `refreshForecast returns error when remote fails`() = runTest(testDispatcher) {
        // Given
        val exception = Exception("Remote Error")
        coEvery { remoteDataSource.getHourlyForecast(any(), any()) } returns Result.Error(exception)

        // When
        val result = repository.refreshForecast(0.0, 0.0)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 0) { localDataSource.saveForecast(any()) }
    }
}