package com.linhphan.lpcore.ui.forecast.daily

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import com.linhphan.lpcore.ui.base.fragment.BaseFragmentActivityViewModel
import com.linhphan.lpcore.ui.forecast.mapper.ForecastUiMapper
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiItem
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class DailyForecastDetailsViewModel @Inject constructor(
    private val getHourlyForecastUseCase: IGetHourlyForecastUseCase,
    private val forecastUiMapper: ForecastUiMapper,
) : BaseFragmentActivityViewModel() {

    private val _hourlyForecastUiState: MutableStateFlow<UiState<List<HourlyForecastUiItem>>> = MutableStateFlow(UiState.Empty)
    val hourlyForecastUiState: StateFlow<UiState<List<HourlyForecastUiItem>>> = _hourlyForecastUiState.asStateFlow()


    fun fetch24HourForecast(lat: Double, lon: Double, timezone: String, selectedDate: Long) {
        val (startDate, endDate) = calculateStartAndEndDateFor24Hour(timezone, selectedDate)

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
                        val filteredList = filter24Hours(hourlyForecastUiItemList, timezone, selectedDate)
                        
                        if (filteredList.isNotEmpty()) {
                             _hourlyForecastUiState.value = UiState.Success(filteredList)
                        } else {
                            _hourlyForecastUiState.value = UiState.Empty
                        }
                    } else {
                        _hourlyForecastUiState.value = UiState.Empty
                    }
                }

                is Result.Error -> {
                    _hourlyForecastUiState.value = UiState.Error(result.exception.message)
                    Timber.e(result.exception, "Error fetching hourly forecast for lat=$lat, lon=$lon")
                }
            }
        }
    }
    
    private fun filter24Hours(items: List<HourlyForecastUiItem>, timezone: String, selectedDate: Long): List<HourlyForecastUiItem> {
        val timeZone = TimeZone.getTimeZone(timezone)
        val calendar = Calendar.getInstance(timeZone)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = timeZone
        val currentDateStr = dateFormat.format(calendar.time)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        
        val selectedDateStr = dateFormat.format(Date(selectedDate * 1000))

        val startIndex = if (selectedDateStr == currentDateStr) {
            currentHour
        } else {
            0
        }

        if (startIndex >= items.size) return emptyList()
        
        return items.drop(startIndex).take(24)
    }

    private fun calculateStartAndEndDateFor24Hour(timezone: String, selectedDate: Long): Pair<String, String> {
        var timeZone = TimeZone.getTimeZone(timezone)
        if (timeZone.id == "GMT" &&
            !timezone.equals("GMT", ignoreCase = true) &&
            !timezone.equals("UTC", ignoreCase = true)
        ) {
            timeZone = TimeZone.getDefault()
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = timeZone

        val startDate = dateFormat.format(Date(selectedDate * 1000))
        
        val selectedDateCal = Calendar.getInstance(timeZone)
        selectedDateCal.timeInMillis = selectedDate * 1000
        
        selectedDateCal.add(Calendar.DAY_OF_YEAR, 1)
        val endDate = dateFormat.format(selectedDateCal.time)

        return startDate to endDate
    }
}
