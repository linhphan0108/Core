package com.linhphan.lpcore.ui.twosidepannels.pannelone

import com.linhphan.lpcore.MainDispatcherRule
import com.linhphan.lpcore.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PanelOneFragmentViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loadCakes emits Loading then Success`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = PanelOneFragmentViewModel(testDispatcher)

        // Initial state should be loading because init block calls loadCakes -> emits Loading -> hits delay
        assertTrue(viewModel.cakes.value is Result.Loading)

        // Advance time to pass the delay
        advanceTimeBy(1001)
        
        // Check if success
        val result = viewModel.cakes.value
        assertTrue(result is Result.Success)
        assertEquals(5, (result as Result.Success).data.size)
        assertEquals("Chocolate Cake", result.data[0].name)
    }
}