package com.linhphan.lpcore.ui.twosidepannels.pannelone

import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.domain.model.Cake
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.ui.base.fragment.BaseFragmentActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Linh Phan
 * @since 2025-11-20
 */
@HiltViewModel
class PanelOneFragmentViewModel @Inject constructor(
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseFragmentActivityViewModel(){

    private val _cakes = MutableStateFlow<List<Cake>?>(null)
    val cakes: StateFlow<List<Cake>?> = _cakes.asStateFlow()

    init {
        loadCakes()
    }

    fun loadCakes() {
        viewModelScope.launch(ioDispatcher) {
//            _cakes.value = Result.Loading
            try {
                // Simulate network delay
                delay(1000)
                val cakeList = listOf(
                    Cake(1, "Chocolate Cake", "Delicious chocolate cake"),
                    Cake(2, "Strawberry Cake", "Fresh strawberry cake"),
                    Cake(3, "Vanilla Cake", "Classic vanilla cake"),
                    Cake(4, "Red Velvet Cake", "Rich red velvet cake"),
                    Cake(5, "Carrot Cake", "Healthy carrot cake")
                )
                _cakes.value = cakeList
            } catch (e: Exception) {
                Timber.e(e)
//                _cakes.value = Result.Error(e)
            }
        }
    }
}