package com.linhphan.lpcore.data.forecast.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the response from the weather forecast API (5 day / 3 hour forecast).
 *
 * @property cod Internal parameter.
 * @property message Internal parameter.
 * @property cnt A number of timestamps returned in the API response.
 * @property list A list of forecast items.
 * @property city The city information.
 */
data class ForecastResponseDto(
    @SerializedName("cod") val cod: String?,
    @SerializedName("message") val message: Int?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("list") val list: List<ForecastDto>?,
    @SerializedName("city") val city: CityDto?
)

/**
 * Represents a single forecast item in the list (3-hour interval).
 *
 * @property dt Time of data forecasted, unix, UTC.
 * @property main Main weather data.
 * @property weather List of weather conditions.
 * @property clouds Cloudiness data.
 * @property wind Wind data.
 * @property visibility Average visibility, metres.
 * @property pop Probability of precipitation.
 * @property rain Rain data.
 * @property snow Snow data.
 * @property sys System data (part of day).
 * @property dtTxt Time of data forecasted, ISO, UTC.
 */
data class ForecastDto(
    @SerializedName("dt") val dt: Long?,
    @SerializedName("main") val main: MainDto?,
    @SerializedName("weather") val weather: List<WeatherDto>?,
    @SerializedName("clouds") val clouds: CloudsDto?,
    @SerializedName("wind") val wind: WindDto?,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("pop") val pop: Double?,
    @SerializedName("rain") val rain: RainDto?,
    @SerializedName("snow") val snow: SnowDto?,
    @SerializedName("sys") val sys: SysDto?,
    @SerializedName("dt_txt") val dtTxt: String?
)

/**
 * Main weather data.
 *
 * @property temp Temperature.
 * @property feelsLike Human perception of weather.
 * @property tempMin Minimum temperature.
 * @property tempMax Maximum temperature.
 * @property pressure Atmospheric pressure.
 * @property seaLevel Atmospheric pressure on the sea level.
 * @property grndLevel Atmospheric pressure on the ground level.
 * @property humidity Humidity, %.
 * @property tempKf Internal parameter.
 */
data class MainDto(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax: Double?,
    @SerializedName("pressure") val pressure: Int?,
    @SerializedName("sea_level") val seaLevel: Int?,
    @SerializedName("grnd_level") val grndLevel: Int?,
    @SerializedName("humidity") val humidity: Int?,
    @SerializedName("temp_kf") val tempKf: Double?
)

/**
 * Weather condition details.
 */
data class WeatherDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("main") val main: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)

/**
 * Cloudiness data.
 */
data class CloudsDto(
    @SerializedName("all") val all: Int?
)

/**
 * Wind data.
 */
data class WindDto(
    @SerializedName("speed") val speed: Double?,
    @SerializedName("deg") val deg: Int?,
    @SerializedName("gust") val gust: Double?
)

/**
 * Rain data.
 */
data class RainDto(
    @SerializedName("3h") val threeH: Double?
)

/**
 * Snow data.
 */
data class SnowDto(
    @SerializedName("3h") val threeH: Double?
)

/**
 * System data.
 */
data class SysDto(
    @SerializedName("pod") val pod: String?
)

/**
 * Represents the city information.
 */
data class CityDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: CoordDto?,
    @SerializedName("country") val country: String?,
    @SerializedName("population") val population: Int?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)

/**
 * Represents the geo location coordinates.
 */
data class CoordDto(
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?
)