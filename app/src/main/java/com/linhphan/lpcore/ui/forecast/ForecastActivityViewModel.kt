package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.ForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getForecastUseCase: IGetForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiModel())
    val uiState: StateFlow<ForecastUiModel> = _uiState.asStateFlow()

    fun fetchForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            getForecastUseCase(IGetForecastUseCase.Params(lat, lon)).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    }
                    is Result.Success -> {
                        val uiModel = forecastUiMapper.mapToUiModel(result.data)
                        _uiState.value = uiModel.copy(isLoading = false)
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                        Timber.e(result.exception, "Error fetching forecast for lat=$lat, lon=$lon")
                    }
                }
            }
        }
    }
}