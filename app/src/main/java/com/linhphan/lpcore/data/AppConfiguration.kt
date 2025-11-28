package com.linhphan.lpcore.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfiguration @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_debug_config", Context.MODE_PRIVATE)

    var isEmbeddedServerEnabled: Boolean
        get() = prefs.getBoolean(KEY_EMBEDDED_SERVER, true) // Default to true in debug
        set(value) = prefs.edit().putBoolean(KEY_EMBEDDED_SERVER, value).apply()

    companion object {
        private const val KEY_EMBEDDED_SERVER = "is_embedded_server_enabled"
    }
}