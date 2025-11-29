package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
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
class GetHourlyForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: GetHourlyForecastUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetHourlyForecastUseCase(repository, testDispatcher)
    }

    @Test
    fun `invoke delegates to repository`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val timezone = "UTC"
        val startDate = "2024-01-01"
        val endDate = "2024-01-02"
        val params = IGetHourlyForecastUseCase.Params(lat, lon, timezone, startDate, endDate)
        
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        val expectedResult = Result.Success(forecasts)
        
        coEvery { 
            repository.getHourlyForecast(lat, lon, timezone, startDate, endDate) 
        } returns expectedResult

        // When
        val result = useCase(params)

        // Then
        assertEquals(expectedResult, result)
    }
}
