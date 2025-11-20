package com.linhphan.lpcore.ui.main

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {
    // ViewModel logic
}