package com.linhphan.lpcore.ui.forecast

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
    lateinit var getCurrentForecastUseCase: IGetCurrentForecastUseCase

    @MockK
    lateinit var getHourlyForecastUseCase: IGetHourlyForecastUseCase

    @MockK
    lateinit var getDailyForecastUseCase: IGetDailyForecastUseCase

    @MockK
    lateinit var forecastUiMapper: ForecastUiMapper

    private lateinit var viewModel: ForecastActivityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastActivityViewModel(getForecastUseCase, getHourlyForecastUseCase, getCurrentForecastUseCase, getDailyForecastUseCase, forecastUiMapper)
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
        val hourlyForecasts = HourlyForecasts(CityInfo("City", "Country"), emptyList())
        val uiModel = listOf(HourlyForecastUiItem(
            hour = "10:00",
            description = "Sunny",
            tempMax = "30",
            precipitationProbability = "0%",
            iconRes = 0
        ))
        
        coEvery { getForecastUseCase(IGetForecastUseCase.Params(lat, lon)) } returns flowOf(Result.Success(hourlyForecasts))
        every { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns uiModel
        
        // When
        viewModel.fetchForecast(lat, lon)
        advanceUntilIdle()
        
        // Then
        assertEquals(UiState.Success(uiModel), viewModel.hourlyForecastUiState.value)
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
        val currentState = viewModel.hourlyForecastUiState.value
        assert(currentState is UiState.Error)
        assertEquals(errorMessage, (currentState as UiState.Error).message)
    }
    
    @Test
    fun `fetch24HourlyForecast success updates uiState`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val hourlyForecasts = HourlyForecasts(CityInfo("City", "Country"), emptyList())
        val uiModel = listOf(HourlyForecastUiItem(
            hour = "10:00",
            description = "Sunny",
            tempMax = "30",
            precipitationProbability = "0%",
            iconRes = 0
        ))
        
        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(hourlyForecasts)
        every { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns uiModel
        
        // When
        viewModel.fetch24HourlyForecast(lat, lon, timezone)
        advanceUntilIdle()
        
        // Then
        assertEquals(UiState.Success(uiModel), viewModel.hourlyForecastUiState.value)
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
        val currentState = viewModel.hourlyForecastUiState.value
        assert(currentState is UiState.Error)
        assertEquals(errorMessage, (currentState as UiState.Error).message)
    }

    @Test
    fun `cityUiModel change triggers fetch24HourlyForecast`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val newCity = CityUiModel("Hanoi", "Vietnam", CoordinateUiModel(lat, lon, timezone))
        val hourlyForecasts = HourlyForecasts(CityInfo("Hanoi", "Vietnam"), emptyList())
        val uiModel = listOf(HourlyForecastUiItem(
            hour = "10:00",
            description = "Sunny",
            tempMax = "30",
            precipitationProbability = "0%",
            iconRes = 0
        ))
        val dailyForecasts = DailyForecasts(CityInfo("Hanoi", "Vietnam"), emptyList())
        val dailyUiModel = listOf(DailyForecastUiItem(
            day = "Mon",
            date = "25/11",
            description = "Sunny",
            tempMax = "30",
            tempMin = "20",
            precipitationProbability = "0%",
            iconRes = 0
        ))

        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(hourlyForecasts)
        coEvery { getCurrentForecastUseCase(any()) } returns Result.Error(Exception("Ignore"))
        coEvery { getDailyForecastUseCase(any()) } returns Result.Success(dailyForecasts)
        every { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns uiModel
        every { forecastUiMapper.mapToUiModel(dailyForecasts) } returns dailyUiModel

        // When
        viewModel.cityUiModel = newCity
        advanceUntilIdle()

        // Then
        coVerify { getHourlyForecastUseCase(any()) }
        coVerify { getDailyForecastUseCase(any()) }
        assertEquals(UiState.Success(uiModel), viewModel.hourlyForecastUiState.value)
        assertEquals(UiState.Success(dailyUiModel), viewModel.dailyForecastUiState.value)
    }

    @Test
    fun `fetch10DayDailyForecast success updates uiState`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val dailyForecasts = DailyForecasts(CityInfo("City", "Country"), emptyList())
        val uiModel = listOf(DailyForecastUiItem(
            day = "Mon",
            date = "25/11",
            description = "Sunny",
            tempMax = "30",
            tempMin = "20",
            precipitationProbability = "0%",
            iconRes = 0
        ))

        coEvery { getDailyForecastUseCase(any()) } returns Result.Success(dailyForecasts)
        every { forecastUiMapper.mapToUiModel(dailyForecasts) } returns uiModel

        // When
        viewModel.fetch10DayDailyForecast(lat, lon, timezone)
        advanceUntilIdle()

        // Then
        assertEquals(UiState.Success(uiModel), viewModel.dailyForecastUiState.value)
    }

    @Test
    fun `fetch10DayDailyForecast error updates uiState with error message`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val errorMessage = "Daily Forecast Error"

        coEvery { getDailyForecastUseCase(any()) } returns Result.Error(Exception(errorMessage))

        // When
        viewModel.fetch10DayDailyForecast(lat, lon, timezone)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.dailyForecastUiState.value
        assert(currentState is UiState.Error)
        assertEquals(errorMessage, (currentState as UiState.Error).message)
    }
}
