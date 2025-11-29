package com.linhphan.lpcore.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivityForecastBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.UiState
import com.linhphan.lpcore.ui.forecast.model.cities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>() {

    private val viewModel: ForecastActivityViewModel by viewModels()
    private val forecastAdapter = ForecastAdapter()
    private val dailyForecastAdapter = DailyForecastAdapter()

    override fun getViewBinding(): ActivityForecastBinding {
        return ActivityForecastBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Set a default city to start with
            val cityUiModel = cities.random()
            viewModel.cityUiModel = cityUiModel
            binding.etCityName.setText(cityUiModel.name)
            binding.tvCityTitle.text = getFormatedCityCountry(cityUiModel)
        }
    }

    override fun setupViews() {
        // Show the back arrow in the Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvForecast.adapter = forecastAdapter
        binding.rvDailyForecast.adapter = dailyForecastAdapter

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
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
                Timber.i("etCityName focused")
            }
        }
        binding.etCityName.setOnClickListener {
            binding.etCityName.showDropDown()
            Timber.i("etCityName clicked")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun setupObservers() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentForecastUiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
//                            binding.progressBar.isVisible = true
                        }

                        is UiState.Empty -> {
//                            binding.progressBar.isVisible = false
                        }

                        is UiState.Success -> {
                            val currentForecastUiModel = uiState.data
                            binding.tvCurrentTemp.text = currentForecastUiModel.temp
                            binding.tvCurrentWeatherCondition.text =
                                currentForecastUiModel.weatherCondition
                            binding.tvFeelsLike.text = currentForecastUiModel.feelsLike
                            binding.tvHighLow.text = currentForecastUiModel.highLow
                            binding.ivCurrentWeatherIcon.setImageResource(currentForecastUiModel.iconRes)
//                            binding.progressBar.isVisible = false
                        }

                        is UiState.Error -> {
//                            binding.progressBar.isVisible = false
                            Toast.makeText(
                                this@ForecastActivity,
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
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
                                this@ForecastActivity,
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
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
                                this@ForecastActivity,
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
        return getString(R.string.city_country_format, cityUiModel.name, cityUiModel.country)
    }
}
