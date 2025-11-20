package com.linhphan.LPCore.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}