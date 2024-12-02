package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SignalStrengthDao {
    @Query("SELECT * FROM signal_strengths ORDER BY strength DESC")
    fun getAllSignalStrengths(): LiveData<List<SignalStrength>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignalStrength(signal: SignalStrength)

    @Delete
    suspend fun deleteSignalStrength(signal: SignalStrength)
}
