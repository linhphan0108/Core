package com.linhphan.lpcore.ui.forecast.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.databinding.FragmentDailyForecastDetailsBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import com.linhphan.lpcore.ui.forecast.ForecastActivityViewModel
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyForecastDetailsFragment :
    BaseFragment<FragmentDailyForecastDetailsBinding, DailyForecastDetailsViewModel>() {

    override val viewModel: DailyForecastDetailsViewModel by activityViewModels()

    private val forecastActivityViewModel: ForecastActivityViewModel by activityViewModels()

    private val dailyForecastAdapter = DailyForecastAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDailyForecastDetailsBinding {
        return FragmentDailyForecastDetailsBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        binding.rvDailyForecastDetails.adapter = dailyForecastAdapter
         binding.cardDailyForecastDetails.isVisible = isLargeScreen.not()
    }

    override fun setupObservers() {
        if (isLargeScreen.not()) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    forecastActivityViewModel.dailyForecastUiState.collect { uiState ->
                        if (uiState is UiState.Success) {
                            dailyForecastAdapter.submitList(uiState.data)

                            // Scroll to selected item if arguments are passed
                            arguments?.getString(ARG_SELECTED_DATE)?.let { selectedDate ->
                                val position = uiState.data.indexOfFirst { it.date == selectedDate }
                                if (position != -1) {
                                    binding.rvDailyForecastDetails.scrollToPosition(position)

                                    val updatedList = uiState.data.map {
                                        it.copy(isSelected = it.date == selectedDate)
                                    }
                                    dailyForecastAdapter.submitList(updatedList)
                                }
                            }
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

        fun newInstance(
            selectedDate: String? = null,
            isLargeScreen: Boolean = false
        ): DailyForecastDetailsFragment {
            return DailyForecastDetailsFragment().apply {
                arguments = Bundle().apply {
                    selectedDate?.let { putString(ARG_SELECTED_DATE, selectedDate) }
                    putBoolean(ARG_IS_LARGE_SCREEN, isLargeScreen)
                }
            }
        }
    }
}
