package com.linhphan.lpcore.ui.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.linhphan.lpcore.databinding.ActivityMainBinding
import com.linhphan.lpcore.ui.base.BaseActivity
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