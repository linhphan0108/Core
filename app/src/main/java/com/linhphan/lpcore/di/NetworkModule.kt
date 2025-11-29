package com.linhphan.lpcore.di

import com.linhphan.lpcore.BuildConfig
import com.linhphan.lpcore.data.AppConfiguration
import com.linhphan.lpcore.data.KtorEmbeddedServer
import com.linhphan.lpcore.data.forecast.remote.ForecastApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            try {
                if (message.startsWith("{")) {
                    Timber.tag("OkHttp").d(JSONObject(message).toString(4))
                } else if (message.startsWith("[")) {
                    Timber.tag("OkHttp").d(JSONArray(message).toString(4))
                } else {
                    Timber.tag("OkHttp").d(message)
                }
            } catch (_: JSONException) {
                Timber.tag("OkHttp").d(message)
            }
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        ktorEmbeddedServer: KtorEmbeddedServer,
        appConfiguration: AppConfiguration,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val baseUrl = if (BuildConfig.DEBUG && appConfiguration.isEmbeddedServerEnabled) {
            ktorEmbeddedServer.baseUrl
        } else {
            "https://api.openweathermap.org/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideForecastApiService(retrofit: Retrofit): ForecastApiService {
        return retrofit.create(ForecastApiService::class.java)
    }
}