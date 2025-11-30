package com.linhphan.lpcore.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivityForecastBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.forecast.daily.DailyForecastFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>() {

    private val viewModel: ForecastActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityForecastBinding {
        return ActivityForecastBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DailyForecastFragment())
                .commit()
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
        // Observers are now handled in the fragment
    }
}
