package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.model.CurrentForecastResponseDto
import com.linhphan.lpcore.data.forecast.model.HourlyForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApiService {

    /**
     * Fetches the current weather forecast data from Open-Meteo.
     */
    @GET("v1/forecast")
    suspend fun getCurrentForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") timezone: String = "Asia/Bangkok",
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m",
    ): CurrentForecastResponseDto

    /**
     * Fetches the hourly forecast data from Open-Meteo.
     *
     * @param lat Latitude.
     * @param lon Longitude.
     * @param hourly Parameters to request (e.g. temperature_2m, weathercode).
     * @param timezone Timezone for the forecast.
     * @param startDate Start date (YYYY-MM-DD).
     * @param endDate End date (YYYY-MM-DD).
     * @return A [HourlyForecastResponseDto] containing the weather forecast data.
     */
    @GET("v1/forecast")
    suspend fun getHourlyForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high,visibility,evapotranspiration,et0_fao_evapotranspiration,vapour_pressure_deficit",
        @Query("timezone") timezone: String = "Asia/Bangkok",
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
    ): HourlyForecastResponseDto
}
