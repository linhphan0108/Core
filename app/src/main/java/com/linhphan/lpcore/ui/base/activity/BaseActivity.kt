package com.linhphan.lpcore.ui.base.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.linhphan.lpcore.R

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract fun getViewBinding(): VB
    abstract fun setupViews()
    abstract fun setupObservers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        setupViews()
        setupObservers()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // This ensures your layout doesn't go under the status bar
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // REPLACEMENT: Set the Window background.
            // Since the status bar is transparent and we padded the content down,
            // this color will show through in the status bar area.
            window.setBackgroundDrawableResource(R.color.primaryLightColor)

            // Force icons to be dark (true) or light (false) depending on your color
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}