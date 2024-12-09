package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SignalStrengthDao {

    // Query to fetch all signal strengths
    @Query("SELECT * FROM stiprumai")
    fun getAllSignalStrengths(): LiveData<List<SignalStrength>>

    // Insert a single signal strength
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignalStrength(signalStrength: SignalStrength): Long

    // Insert multiple signal strengths
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSignalStrengths(signalStrengths: List<SignalStrength>)

    // Update a signal strength
    @Update
    suspend fun updateSignalStrength(signalStrength: SignalStrength)

    // Delete a signal strength
    @Delete
    suspend fun deleteSignalStrength(signalStrength: SignalStrength)

    // Clear all signal strengths
    @Query("DELETE FROM stiprumai")
    suspend fun clearSignalStrengths()
}
