package com.linhphan.lpcore.di

import com.linhphan.lpcore.BuildConfig
import com.linhphan.lpcore.data.AppConfiguration
import com.linhphan.lpcore.data.KtorEmbeddedServer
import com.linhphan.lpcore.data.forecast.remote.ForecastApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        ktorEmbeddedServer: KtorEmbeddedServer,
        appConfiguration: AppConfiguration
    ): Retrofit {
        val baseUrl = if (BuildConfig.DEBUG && appConfiguration.isEmbeddedServerEnabled) {
            ktorEmbeddedServer.baseUrl
        } else {
            "https://api.openweathermap.org/"
        }
        
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideForecastApiService(retrofit: Retrofit): ForecastApiService {
        return retrofit.create(ForecastApiService::class.java)
    }
}