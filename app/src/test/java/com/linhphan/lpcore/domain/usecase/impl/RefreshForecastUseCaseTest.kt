package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IRefreshForecastUseCase
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
class RefreshForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: RefreshForecastUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = RefreshForecastUseCase(repository, testDispatcher)
    }

    @Test
    fun `invoke delegates to repository`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val expectedResult = Result.Success(Unit)
        
        coEvery { repository.refreshForecast(lat, lon) } returns expectedResult

        // When
        val result = useCase(lat, lon)

        // Then
        assertEquals(expectedResult, result)
    }
}