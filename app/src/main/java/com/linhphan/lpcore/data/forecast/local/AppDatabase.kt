package com.linhphan.lpcore.data.forecast.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForecastEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}