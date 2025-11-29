package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.ForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getForecastUseCase: IGetForecastUseCase,
    private val getHourlyForecastUseCase: IGetHourlyForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiModel())
    val uiState: StateFlow<ForecastUiModel> = _uiState.asStateFlow()
    
    private var fetchForecastJob: Job? = null

    var cityUiModel: CityUiModel by Delegates.observable(
        initialValue = CityUiModel("", "", CoordinateUiModel(0.0, 0.0))
    ) { _, old, new ->
        if (old == new) return@observable
        fetch24HourlyForecast(new.coordinate.lat, new.coordinate.lon, new.coordinate.timezone)
    }

    fun fetchForecast(lat: Double, lon: Double) {
        fetchForecastJob?.cancel()
        fetchForecastJob = viewModelScope.launch {
            getForecastUseCase(IGetForecastUseCase.Params(lat, lon)).collect { result ->
                 when (result) {
                    is Result.Success -> {
                        _uiState.value = forecastUiMapper.mapToUiModel(cityUiModel, result.data)
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

    fun fetch24HourlyForecast(lat: Double, lon: Double, timezone: String) {
        val (startDate, endDate) = calculateStartAndEndDate(timezone)
        
        viewModelScope.launch {
            val result = getHourlyForecastUseCase(IGetHourlyForecastUseCase.Params(lat, lon, timezone, startDate, endDate))
            when (result) {
                is Result.Success -> {
                    _uiState.value = forecastUiMapper.mapToUiModel(cityUiModel, result.data)
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

    private fun calculateStartAndEndDate(timezone: String): Pair<String, String> {
        val zoneId = try {
            ZoneId.of(timezone)
        } catch (e: Exception) {
            ZoneId.systemDefault()
        }
        val now = LocalDate.now(zoneId)
        val tomorrow = now.plusDays(1)
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        return now.format(formatter) to tomorrow.format(formatter)
    }
}
