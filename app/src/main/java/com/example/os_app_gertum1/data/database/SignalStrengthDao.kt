package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SignalStrengthDao {

    @Query("SELECT * FROM stiprumai")
    fun getAllSignalStrengths(): LiveData<List<SignalStrength>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignalStrength(signalStrength: SignalStrength): Long  // Return type should be Long

    @Update
    suspend fun updateSignalStrength(signalStrength: SignalStrength)

    @Delete
    suspend fun deleteSignalStrength(signalStrength: SignalStrength)
}
