package com.linhphan.lpcore.ui.main

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.linhphan.lpcore.databinding.ActivityMainBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.forecast.ForecastActivity
import com.linhphan.lpcore.ui.twosidepannels.TwoSideScreenActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun setupViews() {
        binding.btnTwoSideScreen.setOnClickListener {
            startActivity(Intent(this, TwoSideScreenActivity::class.java))
        }

        binding.btnForecast.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun setupObservers() {
        // Observe ViewModel state here
    }
}