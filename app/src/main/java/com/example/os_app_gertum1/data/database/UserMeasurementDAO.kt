package com.example.os_app_gertum1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserMeasurementDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: UserMeasurement)

    @Query("SELECT * FROM user_measurement_table ORDER BY id DESC")
    suspend fun getAllMeasurements(): List<UserMeasurement>

    @Query("SELECT * FROM user_measurement_table WHERE userMacAddress = :macAddress ORDER BY id DESC")
    suspend fun getMeasurementsForMac(macAddress: String): List<UserMeasurement>

    @Query("SELECT * FROM user_measurement_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastMeasurement(): UserMeasurement?

    @Query("SELECT * FROM user_measurement_table WHERE id = :id")
    suspend fun getMeasurementById(id: Int): UserMeasurement?

    @Update
    suspend fun updateMeasurement(measurement: UserMeasurement)

}
