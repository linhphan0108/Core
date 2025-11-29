package com.linhphan.lpcore.ui.forecast

import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
class ForecastActivityViewModelTest {

    private lateinit var viewModel: ForecastActivityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastActivityViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
