package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.model.ForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApiService {
    /**
     * Fetches the 3-hour interval forecast data.
     *
     * @param lat Latitude. If you need the geocoder to automatic convert city names and zip-codes to geo coordinates and the other way around, please use our Geocoding API.
     * @param lon Longitude. If you need the geocoder to automatic convert city names and zip-codes to geo coordinates and the other way around, please use our Geocoding API.
     * @param apiKey Your unique API key (you can always find it on your account page under the "API key" tab).
     * @param count A number of timestamps, which will be returned in the API response (from 1 to 40).
     * @param mode Data format. Possible values are: json, xml. If the mode parameter is empty the format is JSON by default.
     * @param units Units of measurement. standard, metric and imperial units are available. If you do not use the units parameter, standard units will be applied by default.
     * @param lang Language code.
     * @return A [ForecastResponseDto] containing the weather forecast data.
     */
    @GET("data/2.5/forecast")
    suspend fun getThreeHourIntervalForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("cnt") count: Int = 40,
        @Query("mode") mode: String = "json",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en",
    ): ForecastResponseDto
}