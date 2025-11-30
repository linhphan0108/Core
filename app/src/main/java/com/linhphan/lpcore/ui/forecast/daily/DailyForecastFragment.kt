package com.linhphan.lpcore.ui.forecast.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.databinding.FragmentDailyForecastBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.UiState
import com.linhphan.lpcore.ui.forecast.model.cities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DailyForecastFragment : BaseFragment<FragmentDailyForecastBinding, DailyForecastFragViewModel>() {

    override val viewModel: DailyForecastFragViewModel by viewModels()

    private val forecastAdapter = ForecastAdapter()
    private val dailyForecastAdapter = DailyForecastAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDailyForecastBinding {
        return FragmentDailyForecastBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set a default city to start with if not already set
        if (viewModel.cityUiModel.name.isEmpty()) {
            val cityUiModel = cities.random()
            viewModel.cityUiModel = cityUiModel
        }
    }

    override fun setupViews() {
        binding.rvForecast.adapter = forecastAdapter
        binding.rvDailyForecast.adapter = dailyForecastAdapter

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.etCityName.setAdapter(adapter)
        binding.etCityName.setOnItemClickListener { parent, _, position, _ ->
            val cityUiModel = parent.getItemAtPosition(position) as CityUiModel
            viewModel.cityUiModel = cityUiModel
            // Clear focus to hide keyboard if needed
            binding.etCityName.clearFocus()
            binding.tvCityTitle.text = getFormatedCityCountry(cityUiModel)
        }

        // Show dropdown immediately when focused (if empty or not)
        binding.etCityName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.etCityName.showDropDown()
                Timber.Forest.i("etCityName focused")
            }
        }
        binding.etCityName.setOnClickListener {
            binding.etCityName.showDropDown()
            Timber.Forest.i("etCityName clicked")
        }
    }

    override fun onResume() {
        super.onResume()
        // Set initial city info if available in ViewModel
        val cityUiModel = viewModel.cityUiModel
        if (cityUiModel.name.isNotEmpty()) {
            binding.etCityName.setText(cityUiModel.name)
            binding.tvCityTitle.text = getFormatedCityCountry(cityUiModel)
        }
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentForecastUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // binding.progressBar.isVisible = true
                        }

                        is UiState.Empty -> {
                            // binding.progressBar.isVisible = false
                        }

                        is UiState.Success -> {
                            val currentForecastUiModel = uiState.data
                            binding.tvCurrentTemp.text = currentForecastUiModel.temp
                            binding.tvCurrentWeatherCondition.text =
                                currentForecastUiModel.weatherCondition
                            binding.tvFeelsLike.text = currentForecastUiModel.feelsLike
                            binding.tvHighLow.text = currentForecastUiModel.highLow
                            binding.ivCurrentWeatherIcon.setImageResource(currentForecastUiModel.iconRes)
                            // binding.progressBar.isVisible = false
                        }

                        is UiState.Error -> {
                            // binding.progressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
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
                            binding.hourlyForecastprogressBar.isVisible = true
                            binding.rvForecast.isVisible = false
                        }

                        is UiState.Empty -> {
                            binding.hourlyForecastprogressBar.isVisible = false
                        }

                        is UiState.Success -> {
                            forecastAdapter.updateData(uiState.data)
                            binding.hourlyForecastprogressBar.isVisible = false
                            binding.rvForecast.isVisible = true
                        }

                        is UiState.Error -> {
                            binding.hourlyForecastprogressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailyForecastUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.dailyForecastProgressBar.isVisible = true
                            binding.rvDailyForecast.isVisible = false
                        }

                        is UiState.Empty -> {
                            binding.dailyForecastProgressBar.isVisible = false
                        }

                        is UiState.Success -> {
                            dailyForecastAdapter.submitList(uiState.data)
                            binding.dailyForecastProgressBar.isVisible = false
                            binding.rvDailyForecast.isVisible = true
                        }

                        is UiState.Error -> {
                            binding.dailyForecastProgressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun getFormatedCityCountry(cityUiModel: CityUiModel): String {
        return getString(com.linhphan.lpcore.R.string.city_country_format, cityUiModel.name, cityUiModel.country)
    }
}