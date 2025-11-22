package com.linhphan.lpcore.ui.twosidepannels.pannelone

import com.linhphan.lpcore.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class PanelOneFragmentViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loadCakes emits Loading then Success`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = PanelOneFragmentViewModel(testDispatcher)

        // Advance time to pass the delay
        advanceTimeBy(1001)
        
        // Check if success
        val result = viewModel.cakes.value
        assertNotNull(result) {
            assertEquals(5, it.size)
            assertEquals("Chocolate Cake", it[0].name)
        }
    }
}