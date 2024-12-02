package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SignalStrengthDao {
    @Query("SELECT * FROM stiprumai")
    fun getAllSignalStrengths(): LiveData<List<SignalStrength>>


    @Insert
    suspend fun insertSignalStrength(signalStrength: SignalStrength)
}
