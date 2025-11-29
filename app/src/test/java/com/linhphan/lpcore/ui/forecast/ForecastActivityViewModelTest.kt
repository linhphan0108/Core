package com.linhphan.lpcore.ui.forecast

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.ForecastUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ForecastActivityViewModelTest {

    @MockK
    lateinit var getForecastUseCase: IGetForecastUseCase

    @MockK
    lateinit var getHourlyForecastUseCase: IGetHourlyForecastUseCase

    @MockK
    lateinit var forecastUiMapper: ForecastUiMapper

    private lateinit var viewModel: ForecastActivityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastActivityViewModel(getForecastUseCase, getHourlyForecastUseCase, forecastUiMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchForecast success updates uiState`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        val uiModel = ForecastUiModel(cityTitle = "City, Country")
        
        coEvery { getForecastUseCase(IGetForecastUseCase.Params(lat, lon)) } returns flowOf(Result.Success(forecasts))
        every { forecastUiMapper.mapToUiModel(any(), forecasts) } returns uiModel
        
        // When
        viewModel.fetchForecast(lat, lon)
        advanceUntilIdle()
        
        // Then
        assertEquals(uiModel, viewModel.uiState.value)
    }

    @Test
    fun `fetchForecast error updates uiState with error message`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Network Error"
        
        coEvery { getForecastUseCase(IGetForecastUseCase.Params(lat, lon)) } returns flowOf(Result.Error(Exception(errorMessage)))
        
        // When
        viewModel.fetchForecast(lat, lon)
        advanceUntilIdle()
        
        // Then
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }
    
    @Test
    fun `fetch24HourlyForecast success updates uiState`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        val uiModel = ForecastUiModel(cityTitle = "City, Country")
        
        // Match any params for getHourlyForecastUseCase
        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(forecasts)
        every { forecastUiMapper.mapToUiModel(any(), forecasts) } returns uiModel
        
        // When
        viewModel.fetch24HourlyForecast(lat, lon, timezone)
        advanceUntilIdle()
        
        // Then
        assertEquals(uiModel, viewModel.uiState.value)
    }
    
    @Test
    fun `fetch24HourlyForecast error updates uiState with error message`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val errorMessage = "Hourly Forecast Error"
        
        coEvery { getHourlyForecastUseCase(any()) } returns Result.Error(Exception(errorMessage))
        
        // When
        viewModel.fetch24HourlyForecast(lat, lon, timezone)
        advanceUntilIdle()
        
        // Then
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }
}
