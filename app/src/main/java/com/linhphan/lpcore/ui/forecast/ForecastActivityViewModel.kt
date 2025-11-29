package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.CoordinateUiModel
import com.linhphan.lpcore.ui.forecast.model.CurrentForecastUiModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
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
    private val getDailyForecastUseCase: IGetDailyForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _hourlyForecastUiState: MutableStateFlow<UiState<List<HourlyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val hourlyForecastUiState: StateFlow<UiState<List<HourlyForecastUiItem>>> = _hourlyForecastUiState.asStateFlow()

    private val _dailyForecastUiState: MutableStateFlow<UiState<List<DailyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val dailyForecastUiState: StateFlow<UiState<List<DailyForecastUiItem>>> = _dailyForecastUiState.asStateFlow()

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
        fetch10DayDailyForecast(new.coordinate.lat, new.coordinate.lon, new.coordinate.timezone)
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
                        _hourlyForecastUiState.value = UiState.Success(forecastUiMapper.mapToUiModel(domainModel = result.data))
                    }

                    is Result.Error -> {
                        _hourlyForecastUiState.value = UiState.Error(result.exception.message)
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

    fun fetch10DayDailyForecast(lat: Double, lon: Double, timezone: String) {
        val (startDate, endDate) = calculateStartAndEndDateForDaily(timezone)

        viewModelScope.launch {
            _dailyForecastUiState.value = UiState.Loading
            val result = getDailyForecastUseCase(
                IGetDailyForecastUseCase.Params(
                    lat,
                    lon,
                    timezone,
                    startDate,
                    endDate
                )
            )
            when (result) {
                is Result.Success -> {
                    val dailyForecastUiItems = forecastUiMapper.mapToUiModel(domainModel = result.data)
                    if (dailyForecastUiItems.isNotEmpty()) {
                        _dailyForecastUiState.value = UiState.Success(dailyForecastUiItems)
                    } else {
                        _dailyForecastUiState.value = UiState.Empty
                    }
                }

                is Result.Error -> {
                    _dailyForecastUiState.value = UiState.Error(result.exception.message)
                    Timber.e(result.exception, "Error fetching daily forecast for lat=$lat, lon=$lon")
                }
            }
        }
    }

    private fun calculateStartAndEndDate(timezone: String): Pair<String, String> {
        var timeZone = TimeZone.getTimeZone(timezone)
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

    private fun calculateStartAndEndDateForDaily(timezone: String): Pair<String, String> {
        var timeZone = TimeZone.getTimeZone(timezone)
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

        // Fetch 10 days forecast
        calendar.add(Calendar.DAY_OF_YEAR, 10)
        val endDate = dateFormat.format(calendar.time)

        return startDate to endDate
    }
}
