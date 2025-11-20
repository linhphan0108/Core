package com.linhphan.LPCore.ui.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.linhphan.LPCore.databinding.ActivityMainBinding
import com.linhphan.LPCore.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun setupViews() {
        // Setup UI elements here
    }

    override fun setupObservers() {
        // Observe ViewModel state here
    }
}