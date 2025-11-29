package com.linhphan.lpcore.data.forecast.remote

import com.linhphan.lpcore.data.forecast.model.OpenMeteoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApiService {
    /**
     * Fetches the hourly forecast data from Open-Meteo.
     *
     * @param lat Latitude.
     * @param lon Longitude.
     * @param hourly Parameters to request (e.g. temperature_2m, weathercode).
     * @param timezone Timezone for the forecast.
     * @param startDate Start date (YYYY-MM-DD).
     * @param endDate End date (YYYY-MM-DD).
     * @return A [OpenMeteoResponseDto] containing the weather forecast data.
     */
    @GET("v1/forecast")
    suspend fun getHourlyForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high,visibility,evapotranspiration,et0_fao_evapotranspiration,vapour_pressure_deficit",
        @Query("timezone") timezone: String = "Asia/Bangkok",
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
    ): OpenMeteoResponseDto
}
