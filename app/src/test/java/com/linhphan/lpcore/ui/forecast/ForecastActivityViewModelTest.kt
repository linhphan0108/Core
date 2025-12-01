package com.linhphan.lpcore.ui.forecast

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ForecastActivityViewModelTest {

    @MockK
    private lateinit var getDailyForecastUseCase: IGetDailyForecastUseCase

    @MockK
    private lateinit var forecastUiMapper: ForecastUiMapper

    private lateinit var viewModel: ForecastActivityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastActivityViewModel(
            getDailyForecastUseCase,
            forecastUiMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch10DayDailyForecast updates state to Success with data`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val dailyForecasts = mockk<DailyForecasts>()
        val dailyForecastUiItems = listOf(mockk<DailyForecastUiItem>())

        coEvery { getDailyForecastUseCase(any()) } returns Result.Success(dailyForecasts)
        coEvery { forecastUiMapper.mapToUiModel(dailyForecasts) } returns dailyForecastUiItems

        // When
        viewModel.fetch10DayDailyForecast(lat, lon, timezone)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.dailyForecastUiState.value is UiState.Success)
        assertEquals(dailyForecastUiItems, (viewModel.dailyForecastUiState.value as UiState.Success).data)
    }
}
