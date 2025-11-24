package com.linhphan.lpcore

import android.app.Application
import com.linhphan.lpcore.data.KtorEmbeddedServer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CoreApplication : Application() {

    @Inject
    lateinit var ktorEmbeddedServer: KtorEmbeddedServer

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            
            // Start Ktor embedded server in debug mode
            ktorEmbeddedServer.start()
        }
    }
    
    override fun onTerminate() {
        super.onTerminate()
        if (BuildConfig.DEBUG) {
            ktorEmbeddedServer.stop()
        }
    }
}