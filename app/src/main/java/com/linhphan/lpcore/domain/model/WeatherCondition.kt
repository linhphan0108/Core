package com.linhphan.lpcore.domain.model

enum class WeatherCondition(val code: Int, val description: String) {
    CLEAR_SKY(0, "Clear sky"),
    MAINLY_CLEAR(1, "Mainly clear, partly cloudy, and overcast"),
    PARTLY_CLOUDY(2, "Mainly clear, partly cloudy, and overcast"),
    OVERCAST(3, "Mainly clear, partly cloudy, and overcast"),
    FOG(45, "Fog and depositing rime fog"),
    DEPOSITING_RIME_FOG(48, "Fog and depositing rime fog"),
    DRIZZLE_LIGHT(51, "Drizzle: Light, moderate, and dense intensity"),
    DRIZZLE_MODERATE(53, "Drizzle: Light, moderate, and dense intensity"),
    DRIZZLE_DENSE(55, "Drizzle: Light, moderate, and dense intensity"),
    FREEZING_DRIZZLE_LIGHT(56, "Freezing Drizzle: Light and dense intensity"),
    FREEZING_DRIZZLE_DENSE(57, "Freezing Drizzle: Light and dense intensity"),
    RAIN_SLIGHT(61, "Rain: Slight, moderate and heavy intensity"),
    RAIN_MODERATE(63, "Rain: Slight, moderate and heavy intensity"),
    RAIN_HEAVY(65, "Rain: Slight, moderate and heavy intensity"),
    FREEZING_RAIN_LIGHT(66, "Freezing Rain: Light and heavy intensity"),
    FREEZING_RAIN_HEAVY(67, "Freezing Rain: Light and heavy intensity"),
    SNOW_FALL_SLIGHT(71, "Snow fall: Slight, moderate, and heavy intensity"),
    SNOW_FALL_MODERATE(73, "Snow fall: Slight, moderate, and heavy intensity"),
    SNOW_FALL_HEAVY(75, "Snow fall: Slight, moderate, and heavy intensity"),
    SNOW_GRAINS(77, "Snow grains"),
    RAIN_SHOWERS_SLIGHT(80, "Rain showers: Slight, moderate, and violent"),
    RAIN_SHOWERS_MODERATE(81, "Rain showers: Slight, moderate, and violent"),
    RAIN_SHOWERS_VIOLENT(82, "Rain showers: Slight, moderate, and violent"),
    SNOW_SHOWERS_SLIGHT(85, "Snow showers slight and heavy"),
    SNOW_SHOWERS_HEAVY(86, "Snow showers slight and heavy"),
    THUNDERSTORM_SLIGHT_MODERATE(95, "Thunderstorm: Slight or moderate"),
    THUNDERSTORM_HAIL_SLIGHT(96, "Thunderstorm with slight and heavy hail"),
    THUNDERSTORM_HAIL_HEAVY(99, "Thunderstorm with slight and heavy hail"),
    UNKNOWN(-1, "Unknown");

    companion object {
        fun fromCode(code: Int): WeatherCondition {
            return entries.find { it.code == code } ?: UNKNOWN
        }
    }
}