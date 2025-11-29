package com.linhphan.lpcore.data.forecast.model

import com.google.gson.annotations.SerializedName

data class OpenMeteoResponseDto(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("hourly") val hourly: HourlyDto?
)

data class HourlyDto(
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
