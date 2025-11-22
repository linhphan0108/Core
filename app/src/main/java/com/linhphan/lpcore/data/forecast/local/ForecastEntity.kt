package com.linhphan.lpcore.data.forecast.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecasts")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityName: String,
    val country: String,
    val date: Long,
    val tempDay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherDescription: String,
    val icon: String
)