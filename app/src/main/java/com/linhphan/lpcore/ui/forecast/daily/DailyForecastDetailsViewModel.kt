package com.linhphan.lpcore.ui.forecast.daily

import com.linhphan.lpcore.ui.base.fragment.BaseFragmentActivityViewModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DailyForecastDetailsViewModel @Inject constructor() : BaseFragmentActivityViewModel() {

    private val _dailyForecastUiState: MutableStateFlow<UiState<List<DailyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val dailyForecastUiState: StateFlow<UiState<List<DailyForecastUiItem>>> = _dailyForecastUiState.asStateFlow()

    fun updateDailyForecast(items: List<DailyForecastUiItem>) {
        if (items.isNotEmpty()) {
            _dailyForecastUiState.value = UiState.Success(items)
        } else {
            _dailyForecastUiState.value = UiState.Empty
        }
    }
}
