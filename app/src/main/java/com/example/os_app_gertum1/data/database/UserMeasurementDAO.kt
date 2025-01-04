package com.example.os_app_gertum1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserMeasurementDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: UserMeasurement)

    @Query("SELECT * FROM user_measurement_table ORDER BY id DESC")
    suspend fun getAllMeasurements(): List<UserMeasurement>

    @Query("SELECT * FROM user_measurement_table WHERE userMacAddress = :macAddress ORDER BY id DESC")
    suspend fun getMeasurementsForMac(macAddress: String): List<UserMeasurement>
}
