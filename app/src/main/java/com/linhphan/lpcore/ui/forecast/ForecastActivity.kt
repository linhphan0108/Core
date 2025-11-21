package com.linhphan.lpcore.ui.forecast

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivityForecastBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>() {

    private val viewModel: ForecastActivityViewModel by viewModels()
    private val forecastAdapter = ForecastAdapter()

    override fun getViewBinding(): ActivityForecastBinding {
        return ActivityForecastBinding.inflate(LayoutInflater.from(this))
    }

    override fun setupViews() {
        binding.rvForecast.adapter = forecastAdapter

        binding.btnSearch.setOnClickListener {
            val cityName = binding.etCityName.text.toString()
            if (cityName.isNotBlank()) {
                // Note: In a real app, you would use Geocoder to get lat/lon from city name
                // For now, we will just mock the lat/lon for London as an example
                // or you can update the UI to accept lat/lon

                // Mocking London coordinates for demonstration since API requires lat/lon
                val lat = 51.5074
                val lon = -0.1278
                viewModel.fetchForecast(lat, lon)
            }
        }
    }

    override fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.error.collect { errorMsg ->
                        if (errorMsg != null) {
                            Snackbar.make(binding.rvForecast, errorMsg, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }

                launch {
                    viewModel.forecast.collect { forecast ->
                        forecast?.let {
                            binding.tvCityTitle.text = getString(R.string.city_country_format, it.city.name, it.city.country)
                            forecastAdapter.updateData(it.dailyForecasts)
                        }
                    }
                }
            }
        }
    }
}