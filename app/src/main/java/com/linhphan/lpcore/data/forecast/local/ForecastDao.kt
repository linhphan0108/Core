package com.linhphan.lpcore.data.forecast.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecasts")
    fun getAllForecasts(): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(forecasts: List<ForecastEntity>)

    @Transaction
    suspend fun insertForecastsWithLimit(forecasts: List<ForecastEntity>, limit: Int) {
        insertAll(forecasts)
        deleteOldForecasts(limit)
    }

    // Query to delete items that are NOT in the top N latest items
    // Assuming 'date' is your sorting timestamp
    @Query("DELETE FROM forecasts WHERE date NOT IN (SELECT date FROM forecasts ORDER BY date DESC LIMIT :limit)")
    suspend fun deleteOldForecasts(limit: Int)

    @Query("DELETE FROM forecasts")
    suspend fun clearAll()
}