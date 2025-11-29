package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.CurrentForecastUiModel
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getForecastUseCase: IGetForecastUseCase,
    private val getHourlyForecastUseCase: IGetHourlyForecastUseCase,
    private val getCurrentForecastUseCase: IGetCurrentForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _hourlyForecastUiState: MutableStateFlow<UiState<List<HourlyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val hourlyForecastUiState: StateFlow<UiState<List<HourlyForecastUiItem>>> = _hourlyForecastUiState.asStateFlow()

    private val _currentForecastUiState: MutableStateFlow<UiState<CurrentForecastUiModel>> =
        MutableStateFlow(UiState.Empty)
    val currentForecastUiState: StateFlow<UiState<CurrentForecastUiModel>> =
        _currentForecastUiState.asStateFlow()

    private var fetchForecastJob: Job? = null

    var cityUiModel: CityUiModel by Delegates.observable(
        initialValue = CityUiModel("", "", CoordinateUiModel(0.0, 0.0))
    ) { _, old, new ->
        if (old == new) return@observable
        fetchCurrentForecast(new.coordinate.lat, new.coordinate.lon, new.coordinate.timezone)
        fetch24HourlyForecast(new.coordinate.lat, new.coordinate.lon, new.coordinate.timezone)
    }

    fun fetchCurrentForecast(lat: Double, lon: Double, timezone: String) {
        viewModelScope.launch {
            _currentForecastUiState.value = UiState.Loading
            val result =
                getCurrentForecastUseCase(IGetCurrentForecastUseCase.Params(lat, lon, timezone))
            when (result) {
                is Result.Success -> {
                    val currentForecastUiModel = forecastUiMapper.mapToUiModel(domainModel = result.data)
                    _currentForecastUiState.value = UiState.Success(currentForecastUiModel)
                }

                is Result.Error -> {
                    _currentForecastUiState.value = UiState.Error(result.exception.message)
                    Timber.e(result.exception, "Error fetching current forecast")
                }
            }
        }
    }

    fun fetchForecast(lat: Double, lon: Double) {
        fetchForecastJob?.cancel()
        fetchForecastJob = viewModelScope.launch {
            _hourlyForecastUiState.value = UiState.Loading
            getForecastUseCase(IGetForecastUseCase.Params(lat, lon)).collect { result ->
                when (result) {
                    is Result.Success -> {
                        // This use case returns HourlyForecasts but from local DB (flow)
                        // We might need to adjust mapper to support merging or just update hourly part
//                        _hourlyForecastUiState.value =
//                            forecastUiMapper.mapToUiModel(domainModel = result.data)
                    }

                    is Result.Error -> {
//                        _hourlyForecastUiState.value = _hourlyForecastUiState.value.copy(
//                            errorMessage = result.exception.message
//                        )
                        Timber.e(result.exception, "Error fetching forecast for lat=$lat, lon=$lon")
                    }
                }
            }
        }
    }

    fun fetch24HourlyForecast(lat: Double, lon: Double, timezone: String) {
        val (startDate, endDate) = calculateStartAndEndDate(timezone)

        viewModelScope.launch {
            _hourlyForecastUiState.value = UiState.Loading
            val result = getHourlyForecastUseCase(
                IGetHourlyForecastUseCase.Params(
                    lat,
                    lon,
                    timezone,
                    startDate,
                    endDate
                )
            )
            when (result) {
                is Result.Success -> {
                    val hourlyForecastUiItemList = forecastUiMapper.mapToUiModel(domainModel = result.data)
                    if (hourlyForecastUiItemList.isNotEmpty()) {
                        _hourlyForecastUiState.value = UiState.Success(hourlyForecastUiItemList)
                    } else {
                        _hourlyForecastUiState.value = UiState.Empty
                    }
                }

                is Result.Error -> {
                    _hourlyForecastUiState.value = UiState.Error(result.exception.message)
                    Timber.e(result.exception, "Error fetching forecast for lat=$lat, lon=$lon")
                }
            }
        }
    }

    private fun calculateStartAndEndDate(timezone: String): Pair<String, String> {
        var timeZone = TimeZone.getTimeZone(timezone)
        // TimeZone.getTimeZone returns "GMT" if it doesn't understand the ID.
        // If the user actually requested "GMT" or "UTC", that's fine.
        // Otherwise, if we got "GMT" but didn't ask for it, assume it's invalid and fallback to system default.
        if (timeZone.id == "GMT" &&
            !timezone.equals("GMT", ignoreCase = true) &&
            !timezone.equals("UTC", ignoreCase = true)
        ) {
            timeZone = TimeZone.getDefault()
        }

        val calendar = Calendar.getInstance(timeZone)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = timeZone

        val startDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endDate = dateFormat.format(calendar.time)

        return startDate to endDate
    }
}
