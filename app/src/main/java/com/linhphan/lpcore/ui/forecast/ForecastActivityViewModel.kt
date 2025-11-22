package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IRefreshForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.ForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getForecastUseCase: IGetForecastUseCase,
    private val refreshForecastUseCase: IRefreshForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiModel())
    val uiState: StateFlow<ForecastUiModel> = _uiState.asStateFlow()
    
    private var fetchForecastJob: Job? = null

    var coordinateUiModel: CoordinateUiModel by Delegates.observable(
        initialValue = CoordinateUiModel(0.0, 0.0)
    ) { _, old, new ->
        if (old == new) return@observable
        fetchForecast(new.lat, new.lon)
        refreshForecast(new.lat, new.lon)
    }

    fun fetchForecast(lat: Double, lon: Double) {
        fetchForecastJob?.cancel()
        fetchForecastJob = viewModelScope.launch {
            getForecastUseCase(IGetForecastUseCase.Params(lat, lon)).collect { result ->
                 when (result) {
                    is Result.Success -> {
                        _uiState.value = forecastUiMapper.mapToUiModel(result.data)
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.exception.message
                        )
                        Timber.e(result.exception, "Error fetching forecast for lat=$lat, lon=$lon")
                    }
                }
            }
        }
    }

    fun refreshForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = refreshForecastUseCase(lat, lon)) {
                is Result.Error -> {
                     _uiState.value = _uiState.value.copy(
                        errorMessage = result.exception.message
                    )
                    setLoading(false)
                    Timber.e(result.exception, "Error refreshing forecast for lat=$lat, lon=$lon")
                }
                else -> {
                   setLoading(false)
                }
            }
        }
    }
}