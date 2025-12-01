package com.linhphan.lpcore.ui.forecast.daily

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.model.DailyForecast
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.model.HourlyForecast
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.CurrentForecastUiModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DailyForecastFragViewModelTest {

    @MockK
    private lateinit var getForecastUseCase: IGetForecastUseCase

    @MockK
    private lateinit var getHourlyForecastUseCase: IGetHourlyForecastUseCase

    @MockK
    private lateinit var getCurrentForecastUseCase: IGetCurrentForecastUseCase

    @MockK
    private lateinit var forecastUiMapper: ForecastUiMapper

    private lateinit var viewModel: DailyForecastFragViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = DailyForecastFragViewModel(
            getForecastUseCase,
            getHourlyForecastUseCase,
            getCurrentForecastUseCase,
            forecastUiMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when cityUiModel is set, fetches forecasts`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val cityUiModel = CityUiModel("Test City", "TC", CoordinateUiModel(lat, lon, timezone))

        val currentForecast = mockk<CurrentForecast>()
        val currentForecastUiModel = mockk<CurrentForecastUiModel>()
        coEvery { getCurrentForecastUseCase(any()) } returns Result.Success(currentForecast)
        coEvery { forecastUiMapper.mapToUiModel(currentForecast) } returns currentForecastUiModel

        val hourlyForecasts = mockk<HourlyForecasts>()
        val hourlyForecastUiItems = listOf<HourlyForecastUiItem>()
        // Mocking empty list for mapToUiModel to return empty list
        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(hourlyForecasts)
        coEvery { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns hourlyForecastUiItems

        // When
        viewModel.cityUiModel = cityUiModel

        // Then
        // Verify states are updated (loading -> success/empty)
        // Since we use UnconfinedTestDispatcher, the coroutines execute immediately
        
        // Current Forecast
        assertTrue(viewModel.currentForecastUiState.value is UiState.Success)
        assertEquals(currentForecastUiModel, (viewModel.currentForecastUiState.value as UiState.Success).data)

        // Hourly Forecast (Empty list returned in setup)
        assertTrue(viewModel.hourlyForecastUiState.value is UiState.Empty)
    }

    @Test
    fun `fetchCurrentForecast updates state to Success on success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val currentForecast = mockk<CurrentForecast>()
        val currentForecastUiModel = mockk<CurrentForecastUiModel>()
        
        coEvery { getCurrentForecastUseCase(any()) } returns Result.Success(currentForecast)
        coEvery { forecastUiMapper.mapToUiModel(currentForecast) } returns currentForecastUiModel

        // When
        viewModel.fetchCurrentForecast(lat, lon, timezone)

        // Then
        assertTrue(viewModel.currentForecastUiState.value is UiState.Success)
        assertEquals(currentForecastUiModel, (viewModel.currentForecastUiState.value as UiState.Success).data)
    }

    @Test
    fun `fetchCurrentForecast updates state to Error on failure`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val errorMessage = "Network error"
        
        coEvery { getCurrentForecastUseCase(any()) } returns Result.Error(Exception(errorMessage))

        // When
        viewModel.fetchCurrentForecast(lat, lon, timezone)

        // Then
        assertTrue(viewModel.currentForecastUiState.value is UiState.Error)
        assertEquals(errorMessage, (viewModel.currentForecastUiState.value as UiState.Error).message)
    }

    @Test
    fun `fetch24HourlyForecast updates state to Success with data`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val hourlyForecasts = mockk<HourlyForecasts>()
        val hourlyForecastUiItems = listOf(mockk<HourlyForecastUiItem>())

        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(hourlyForecasts)
        coEvery { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns hourlyForecastUiItems

        // When
        viewModel.fetch24HourlyForecast(lat, lon, timezone)

        // Then
        assertTrue(viewModel.hourlyForecastUiState.value is UiState.Success)
        assertEquals(hourlyForecastUiItems, (viewModel.hourlyForecastUiState.value as UiState.Success).data)
    }
    
    @Test
    fun `fetch24HourlyForecast updates state to Empty when list is empty`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val hourlyForecasts = mockk<HourlyForecasts>()
        val hourlyForecastUiItems = listOf<HourlyForecastUiItem>() // Empty mapped list

        coEvery { getHourlyForecastUseCase(any()) } returns Result.Success(hourlyForecasts)
        coEvery { forecastUiMapper.mapToUiModel(hourlyForecasts) } returns hourlyForecastUiItems

        // When
        viewModel.fetch24HourlyForecast(lat, lon, timezone)

        // Then
        assertTrue(viewModel.hourlyForecastUiState.value is UiState.Empty)
    }
    
     @Test
    fun `fetchForecast updates state to Success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val forecastList = mockk<HourlyForecasts>()
        val forecastUiList = listOf<HourlyForecastUiItem>()
        
        coEvery { getForecastUseCase(any()) } returns flowOf(Result.Success(forecastList))
        coEvery { forecastUiMapper.mapToUiModel(forecastList) } returns forecastUiList

        // When
        viewModel.fetchForecast(lat, lon)

        // Then
        assertTrue(viewModel.hourlyForecastUiState.value is UiState.Success)
    }
}
