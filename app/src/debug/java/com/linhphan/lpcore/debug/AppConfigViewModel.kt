package com.linhphan.lpcore.debug

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linhphan.lpcore.data.AppConfiguration
import com.linhphan.lpcore.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AppConfigViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val appConfiguration: AppConfiguration
) : ViewModel() {

    sealed class AppConfigEvent {
        data class DataCleared(val message: String) : AppConfigEvent()
        data class Error(val message: String?) : AppConfigEvent()
        object ConfigurationChanged : AppConfigEvent()
    }

    private val _eventChannel = Channel<AppConfigEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()
    
    private val _isEmbeddedServerEnabled = MutableStateFlow(appConfiguration.isEmbeddedServerEnabled)
    val isEmbeddedServerEnabled = _isEmbeddedServerEnabled.asStateFlow()

    fun setEmbeddedServerEnabled(enabled: Boolean) {
        if (appConfiguration.isEmbeddedServerEnabled != enabled) {
            appConfiguration.isEmbeddedServerEnabled = enabled
            _isEmbeddedServerEnabled.value = enabled

            viewModelScope.launch {
                delay(100) // Delay to allow the configuration is stored into shared prefs before sending the event
                _eventChannel.send(AppConfigEvent.ConfigurationChanged)
            }
        }
    }

    fun clearAppData() {
        viewModelScope.launch(ioDispatcher) {
            try {
                clearCacheInternal()
                clearSharedPrefsInternal()
                clearDatabasesInternal()

                _eventChannel.send(AppConfigEvent.DataCleared("All App Data Cleared"))
            } catch (e: Exception) {
                _eventChannel.send(AppConfigEvent.Error(e.message))
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch(ioDispatcher) {
            try {
                clearCacheInternal()
                _eventChannel.send(AppConfigEvent.DataCleared("Cache Cleared"))
            } catch (e: Exception) {
                _eventChannel.send(AppConfigEvent.Error(e.message))
            }
        }
    }

    fun clearSharedPrefs() {
        viewModelScope.launch(ioDispatcher) {
            try {
                clearSharedPrefsInternal()
                _eventChannel.send(AppConfigEvent.DataCleared("Shared Prefs Cleared"))
            } catch (e: Exception) {
                _eventChannel.send(AppConfigEvent.Error(e.message))
            }
        }
    }

    fun clearDatabases() {
        viewModelScope.launch(ioDispatcher) {
            try {
                clearDatabasesInternal()
                _eventChannel.send(AppConfigEvent.DataCleared("Databases Cleared"))
            } catch (e: Exception) {
                _eventChannel.send(AppConfigEvent.Error(e.message))
            }
        }
    }

    private fun clearCacheInternal() {
        context.cacheDir?.deleteRecursively()
    }

    private fun clearSharedPrefsInternal() {
        val root = context.filesDir?.parentFile
        if (root != null && root.exists()) {
            val sharedPrefs = File(root, "shared_prefs")
            if (sharedPrefs.exists()) {
                sharedPrefs.deleteRecursively()
            }
        }
    }

    private fun clearDatabasesInternal() {
        val root = context.filesDir?.parentFile
        if (root != null && root.exists()) {
            val databases = File(root, "databases")
            if (databases.exists()) {
                databases.deleteRecursively()
            }
        }
    }
}