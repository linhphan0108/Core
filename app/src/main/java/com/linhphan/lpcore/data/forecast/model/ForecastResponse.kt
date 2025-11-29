package com.linhphan.lpcore.data.forecast.model

import com.google.gson.annotations.SerializedName

open class BaseForecastResponseDto(
    @SerializedName("latitude") open val latitude: Double?,
    @SerializedName("longitude") open val longitude: Double?
)

data class HourlyForecastResponseDto(
    @SerializedName("latitude") override val latitude: Double?,
    @SerializedName("longitude") override val longitude: Double?,
    @SerializedName("hourly") val hourly: HourlyForecastDto?
) : BaseForecastResponseDto(latitude, longitude)

data class CurrentForecastResponseDto(
    @SerializedName("latitude") override val latitude: Double?,
    @SerializedName("longitude") override val longitude: Double?,
    @SerializedName("current") val currentForecasts: CurrentForecastDto?
) : BaseForecastResponseDto(latitude, longitude)

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