package com.linhphan.lpcore.ui.forecast.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.databinding.FragmentDailyForecastDetailsBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import com.linhphan.lpcore.ui.forecast.ForecastActivityViewModel
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyForecastDetailsFragment :
    BaseFragment<FragmentDailyForecastDetailsBinding, DailyForecastDetailsViewModel>() {

    override val viewModel: DailyForecastDetailsViewModel by activityViewModels()

    private val forecastActivityViewModel: ForecastActivityViewModel by activityViewModels()

    private val dailyForecastAdapter = DailyForecastAdapter { item ->
        arguments?.getParcelable<CityUiModel>(ARG_CITY)?.let { cityUiModel ->
            viewModel.fetch24HourForecast(
                cityUiModel.coordinate.lat,
                cityUiModel.coordinate.lon,
                cityUiModel.coordinate.timezone,
                item.dateInTimestamp
            )
        }
    }
    private val hourlyForecastAdapter = ForecastAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDailyForecastDetailsBinding {
        return FragmentDailyForecastDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDate = arguments?.getLong(ARG_SELECTED_DATE)
        val cityUiModel = arguments?.getParcelable<CityUiModel>(ARG_CITY)

        if (selectedDate != null && cityUiModel != null) {
            viewModel.fetch24HourForecast(
                cityUiModel.coordinate.lat,
                cityUiModel.coordinate.lon,
                cityUiModel.coordinate.timezone,
                selectedDate
            )
        }
    }

    override fun setupViews() {
        binding.rvDailyForecastDetails.adapter = dailyForecastAdapter
        binding.cardDailyForecastDetails.isVisible = isLargeScreen.not()

        binding.rv24HourForecast.adapter = hourlyForecastAdapter
    }

    override fun setupObservers() {
        if (isLargeScreen.not()) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    forecastActivityViewModel.dailyForecastUiState.collect { uiState ->
                        if (uiState is UiState.Success) {
                            val selectedDate = arguments?.getLong(ARG_SELECTED_DATE)
                            val mappedList = if (selectedDate != null) {
                                uiState.data.map { item ->
                                    item.copy(isSelected = item.dateInTimestamp == selectedDate)
                                }
                            } else {
                                uiState.data
                            }
                            dailyForecastAdapter.submitList(mappedList) {
                                if (selectedDate != null) {
                                    val position = mappedList.indexOfFirst { it.isSelected }
                                    if (position != -1) {
                                        binding.rvDailyForecastDetails.scrollToPosition(position)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hourlyForecastUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar24Hour.isVisible = true
                            binding.rv24HourForecast.isVisible = false
                        }
                        is UiState.Success -> {
                            binding.progressBar24Hour.isVisible = false
                            binding.rv24HourForecast.isVisible = true
                            hourlyForecastAdapter.submitList(uiState.data)
                        }
                        is UiState.Empty -> {
                            binding.progressBar24Hour.isVisible = false
                            binding.rv24HourForecast.isVisible = false
                        }
                        is UiState.Error -> {
                            binding.progressBar24Hour.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private val isLargeScreen: Boolean
        get() = arguments?.getBoolean(ARG_IS_LARGE_SCREEN) ?: false

    companion object {
        const val ARG_SELECTED_DATE = "selected_date"
        const val ARG_IS_LARGE_SCREEN = "is_large_screen"
        const val ARG_CITY = "city"

        fun newInstance(
            selectedDate: Long? = null,
            cityUiModel: CityUiModel? = null,
            isLargeScreen: Boolean = false
        ): DailyForecastDetailsFragment {
            return DailyForecastDetailsFragment().apply {
                arguments = Bundle().apply {
                    selectedDate?.let { putLong(ARG_SELECTED_DATE, selectedDate) }
                    cityUiModel?.let { putParcelable(ARG_CITY, it) }
                    putBoolean(ARG_IS_LARGE_SCREEN, isLargeScreen)
                }
            }
        }
    }
}
