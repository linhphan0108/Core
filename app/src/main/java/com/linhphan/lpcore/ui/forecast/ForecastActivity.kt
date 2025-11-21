package com.linhphan.lpcore.ui.forecast

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
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
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading

                    if (state.errorMessage != null) {
                        Snackbar.make(binding.root, state.errorMessage, Snackbar.LENGTH_LONG).show()
                    }

                    binding.tvCityTitle.text = state.cityTitle
                    forecastAdapter.updateData(state.items)
                }
            }
        }
    }
}