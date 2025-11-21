package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.domain.model.Forecast
import com.linhphan.lpcore.domain.usecase.GetForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : BaseActivityViewModel() {

    private val _forecast = MutableStateFlow<Forecast?>(null)
    val forecast: StateFlow<Forecast?> = _forecast.asStateFlow()

    fun fetchForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            getForecastUseCase(lat, lon).collect { result ->
                when (result) {
                    is Result.Loading -> setLoading(true)
                    is Result.Success -> {
                        setLoading(false)
                        _forecast.value = result.data
                    }
                    is Result.Error -> {
                        setLoading(false)
                        setError(result.exception.message)
                        Timber.e(result.exception, "Error fetching forecast for lat=$lat, lon=$lon")
                    }
                }
            }
        }
    }
}