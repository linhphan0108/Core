package com.linhphan.LPCore.ui.main

import com.linhphan.LPCore.di.IoDispatcher
import com.linhphan.LPCore.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {
    // ViewModel logic
}