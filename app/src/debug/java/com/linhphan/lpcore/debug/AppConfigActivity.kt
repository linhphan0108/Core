package com.linhphan.lpcore.debug

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivityAppConfigBinding
import com.linhphan.lpcore.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_config, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_main_app -> {
                openMainApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupListeners() {
        // Network Configuration Listeners
        binding.rgNetworkType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbRemote -> viewModel.setEmbeddedServerEnabled(false)
                R.id.rbEmbedded -> viewModel.setEmbeddedServerEnabled(true)
            }
        }
        
        // Data Management Listeners
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
                launch {
                    viewModel.eventFlow.collect { event ->
                        when (event) {
                            is AppConfigViewModel.AppConfigEvent.DataCleared -> {
                                Toast.makeText(this@AppConfigActivity, "${event.message}. Restarting...", Toast.LENGTH_SHORT).show()
                                restartApp()
                            }
                            is AppConfigViewModel.AppConfigEvent.Error -> {
                                Toast.makeText(this@AppConfigActivity, "Error: ${event.message}", Toast.LENGTH_LONG).show()
                            }
                            is AppConfigViewModel.AppConfigEvent.ConfigurationChanged -> {
                                Toast.makeText(this@AppConfigActivity, "Configuration saved. Restarting...", Toast.LENGTH_SHORT).show()
                                restartApp()
                            }
                        }
                    }
                }
                
                launch {
                    viewModel.isEmbeddedServerEnabled.collect { isEnabled ->
                        if (isEnabled) {
                            binding.rbEmbedded.isChecked = true
                        } else {
                            binding.rbRemote.isChecked = true
                        }
                    }
                }
            }
        }
    }

    private fun openMainApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        Runtime.getRuntime().exit(0)
    }

    private fun restartApp() {
//        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
//        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
//        Runtime.getRuntime().exit(0)
    }
}