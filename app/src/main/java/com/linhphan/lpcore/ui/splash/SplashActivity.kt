package com.linhphan.lpcore.ui.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ActivitySplashBinding
import com.linhphan.lpcore.ui.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var dotAnimationJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGif()
        startAnimations()
        startDotAnimation()
        navigateToMain()
    }

    override fun onDestroy() {
        super.onDestroy()
        dotAnimationJob?.cancel()
    }

    private fun setupGif() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.ivLogo.load(R.drawable.loading_skateboarding_fast, imageLoader)
    }

    private fun startAnimations() {
        // Scale animation for the logo
        val scaleX = ObjectAnimator.ofFloat(binding.ivLogo, "scaleX", 0.5f, 1.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(binding.ivLogo, "scaleY", 0.5f, 1.2f, 1.0f)
        
        scaleX.duration = 1500
        scaleY.duration = 1500
        
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        scaleX.start()
        scaleY.start()

        // Fade in animation for the text
        val alpha = ObjectAnimator.ofFloat(binding.tvAppName, "alpha", 0f, 1f)
        alpha.duration = 2000
        alpha.startDelay = 500
        alpha.start()
    }

    private fun startDotAnimation() {
        dotAnimationJob = lifecycleScope.launch {
            val baseText = getString(R.string.loading)
            while (isActive) {
                // Loop from 1 to 3 dots
                for (i in 1..3) {
                    // repeat(n) creates a string with n copies of the character
                    val text = "$baseText${".".repeat(i)}"
                    binding.tvAppName.text = text
                    delay(500)
                }
            }
        }
    }

    private fun navigateToMain() {
        lifecycleScope.launch {
            delay(4000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}