package com.linhphan.lpcore.debug

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.databinding.ActivityAppConfigBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppConfigBinding
    private val viewModel: AppConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnClearData.setOnClickListener {
            viewModel.clearAppData()
        }
        binding.btnClearCache.setOnClickListener {
            viewModel.clearCache()
        }
        binding.btnClearSharedPrefs.setOnClickListener {
            viewModel.clearSharedPrefs()
        }
        binding.btnClearDb.setOnClickListener {
            viewModel.clearDatabases()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is AppConfigViewModel.AppConfigEvent.DataCleared -> {
                            Toast.makeText(this@AppConfigActivity, "${event.message}. Restarting...", Toast.LENGTH_SHORT).show()
                            restartApp()
                        }
                        is AppConfigViewModel.AppConfigEvent.Error -> {
                            Toast.makeText(this@AppConfigActivity, "Error: ${event.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    }
}