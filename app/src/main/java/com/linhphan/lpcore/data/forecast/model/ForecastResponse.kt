package com.linhphan.lpcore.data.forecast.model

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponseDto(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("hourly") val hourly: HourlyForecastDto?
)

data class CurrentForecastResponseDto(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("current") val currentForecasts: CurrentForecastDto?
)

data class DailyForecastResponseDto(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("daily") val daily: DailyForecastDto?
)

data class HourlyForecastDto(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("temperature_2m") val temperature2m: List<Double>?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: List<Int>?,
    @SerializedName("dew_point_2m") val dewPoint2m: List<Double>?,
    @SerializedName("apparent_temperature") val apparentTemperature: List<Double>?,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>?,
    @SerializedName("precipitation") val precipitation: List<Double>?,
    @SerializedName("rain") val rain: List<Double>?,
    @SerializedName("showers") val showers: List<Double>?,
    @SerializedName("snowfall") val snowfall: List<Double>?,
    @SerializedName("snow_depth") val snowDepth: List<Double>?,
    @SerializedName("weather_code") val weatherCode: List<Int>?,
    @SerializedName("pressure_msl") val pressureMsl: List<Double>?,
    @SerializedName("surface_pressure") val surfacePressure: List<Double>?,
    @SerializedName("cloud_cover") val cloudCover: List<Int>?,
    @SerializedName("cloud_cover_low") val cloudCoverLow: List<Int>?,
    @SerializedName("cloud_cover_mid") val cloudCoverMid: List<Int>?,
    @SerializedName("cloud_cover_high") val cloudCoverHigh: List<Int>?,
    @SerializedName("visibility") val visibility: List<Double>?,
    @SerializedName("evapotranspiration") val evapotranspiration: List<Double>?,
    @SerializedName("et0_fao_evapotranspiration") val et0FaoEvapotranspiration: List<Double>?,
    @SerializedName("vapour_pressure_deficit") val vapourPressureDeficit: List<Double>?
)

data class CurrentForecastDto(
    @SerializedName("time") val time: String?,
    @SerializedName("temperature_2m") val temperature2m: Double?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: Int?,
    @SerializedName("apparent_temperature") val apparentTemperature: Double?,
    @SerializedName("is_day") val isDay: Int?,
    @SerializedName("precipitation") val precipitation: Double?,
    @SerializedName("rain") val rain: Double?,
    @SerializedName("showers") val showers: Double?,
    @SerializedName("snowfall") val snowfall: Double?,
    @SerializedName("weather_code") val weatherCode: Int?,
    @SerializedName("cloud_cover") val cloudCover: Int?,
    @SerializedName("pressure_msl") val pressureMsl: Double?,
    @SerializedName("surface_pressure") val surfacePressure: Double?,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double?,
    @SerializedName("wind_direction_10m") val windDirection10m: Double?,
    @SerializedName("wind_gusts_10m") val windGusts10m: Double?
)

data class DailyForecastDto(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("weather_code") val weatherCode: List<Int>?,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>?,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>?,
    @SerializedName("apparent_temperature_max") val apparentTemperatureMax: List<Double>?,
    @SerializedName("apparent_temperature_min") val apparentTemperatureMin: List<Double>?,
    @SerializedName("sunrise") val sunrise: List<String>?,
    @SerializedName("sunset") val sunset: List<String>?,
    @SerializedName("uv_index_max") val uvIndexMax: List<Double>?,
    @SerializedName("precipitation_sum") val precipitationSum: List<Double>?,
    @SerializedName("precipitation_probability_max") val precipitationProbabilityMax: List<Int>?,
    @SerializedName("wind_speed_10m_max") val windSpeed10mMax: List<Double>?
)
