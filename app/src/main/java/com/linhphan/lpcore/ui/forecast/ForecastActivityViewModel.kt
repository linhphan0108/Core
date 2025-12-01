package com.linhphan.lpcore.ui.forecast

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import com.linhphan.lpcore.ui.base.activity.BaseActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ForecastActivityViewModel @Inject constructor(
    private val getDailyForecastUseCase: IGetDailyForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper
) : BaseActivityViewModel() {

    private val _dailyForecastUiState: MutableStateFlow<UiState<List<DailyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val dailyForecastUiState: StateFlow<UiState<List<DailyForecastUiItem>>> = _dailyForecastUiState.asStateFlow()

    private val _navigateToDetails = MutableSharedFlow<Pair<DailyForecastUiItem, CityUiModel>>()
    val navigateToDetails: SharedFlow<Pair<DailyForecastUiItem, CityUiModel>> = _navigateToDetails.asSharedFlow()

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

    fun onDailyForecastItemClicked(item: DailyForecastUiItem, cityUiModel: CityUiModel) {
        viewModelScope.launch {
            _navigateToDetails.emit(item to cityUiModel)
        }
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
