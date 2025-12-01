package com.linhphan.lpcore.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivityForecastBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.forecast.daily.DailyForecastDetailsFragment
import com.linhphan.lpcore.ui.forecast.daily.DailyForecastFragment
import com.linhphan.lpcore.ui.forecast.model.CityUiModel
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>() {

    private val viewModel: ForecastActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityForecastBinding {
        return ActivityForecastBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DailyForecastFragment())

            // Check if the details container exists (large screen)
            val largeScreen = isLargeScreen()
            if (largeScreen) {
                transaction.replace(R.id.fragment_container_details, DailyForecastDetailsFragment.newInstance(isLargeScreen = true))
            }

            transaction.commit()
        }
    }

    override fun setupViews() {
        // Show the back arrow in the Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToDetails.collect { (item, cityUiModel) ->
                    navigateToDetails(item, cityUiModel)
                }
            }
        }
    }

    private fun navigateToDetails(item: DailyForecastUiItem, cityUiModel: CityUiModel) {
        val largeScreen = isLargeScreen()
        val detailsFragment = DailyForecastDetailsFragment.newInstance(item.dateInTimestamp, cityUiModel, largeScreen)

        if (largeScreen) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container_details, detailsFragment)
            }
        } else {
            // On smaller screens, navigate to the details fragment
            supportFragmentManager.commit {
                replace(R.id.fragment_container, detailsFragment)
                addToBackStack(null)
            }
        }
    }

    private fun isLargeScreen(): Boolean {
        return binding.root.findViewById<android.view.View>(R.id.fragment_container_details) != null
    }
}
