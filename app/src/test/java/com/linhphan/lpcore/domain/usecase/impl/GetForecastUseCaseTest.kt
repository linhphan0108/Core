package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CityInfo
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: GetForecastUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetForecastUseCase(repository, testDispatcher)
    }

    @Test
    fun `invoke delegates to repository`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val params = IGetForecastUseCase.Params(lat, lon)
        val forecasts = Forecasts(CityInfo("City", "Country"), emptyList())
        val expectedResult = Result.Success(forecasts)
        
        every { repository.getForecast(lat, lon) } returns flowOf(expectedResult)

        // When
        val result = useCase(params).first()

        // Then
        assertEquals(expectedResult, result)
    }
}