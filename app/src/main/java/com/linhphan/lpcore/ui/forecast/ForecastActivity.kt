package com.linhphan.lpcore.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.linhphan.lpcore.databinding.ActivityForecastBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.cities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>() {

    private val viewModel: ForecastActivityViewModel by viewModels()
    private val forecastAdapter = ForecastAdapter()

    override fun getViewBinding(): ActivityForecastBinding {
        return ActivityForecastBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Set a default city (e.g., the first one in your list)
            val defaultCity = cities.random()
            viewModel.coordinateUiModel = defaultCity.coordinate
        }
    }

    override fun setupViews() {
        // Show the back arrow in the Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvForecast.adapter = forecastAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.etCityName.setAdapter(adapter)
        binding.etCityName.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position) as CityUiModel
            viewModel.coordinateUiModel = city.coordinate
            // Clear focus to hide keyboard if needed
            binding.etCityName.clearFocus()
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
                viewModel.isLoading.collect {
                    binding.progressBar.isVisible = it

                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
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