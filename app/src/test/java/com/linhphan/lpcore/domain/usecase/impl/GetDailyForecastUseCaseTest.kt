package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetDailyForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: GetDailyForecastUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetDailyForecastUseCase(repository, testDispatcher)
    }

    @Test
    fun `execute delegates to repository`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2023-01-01"
        val endDate = "2023-01-10"
        val params = IGetDailyForecastUseCase.Params(lat, lon, timezone, startDate, endDate)
        
        val dailyForecasts = DailyForecasts(CityInfo("City", "Country"), emptyList())
        val expectedResult = Result.Success(dailyForecasts)
        
        coEvery { repository.getDailyForecast(lat, lon, timezone, startDate, endDate) } returns expectedResult

        // When
        val result = useCase(params)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `execute returns error from repository`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "Asia/Bangkok"
        val startDate = "2023-01-01"
        val endDate = "2023-01-10"
        val params = IGetDailyForecastUseCase.Params(lat, lon, timezone, startDate, endDate)
        
        val exception = Exception("Network Error")
        val expectedResult = Result.Error(exception)
        
        coEvery { repository.getDailyForecast(lat, lon, timezone, startDate, endDate) } returns expectedResult

        // When
        val result = useCase(params)

        // Then
        assertEquals(expectedResult, result)
    }
}
