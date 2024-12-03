package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MeasurementDao {

    @Query("SELECT * FROM matavimai")
    fun getAllMeasurements(): LiveData<List<Measurement>>

    @Query("SELECT * FROM matavimai WHERE id = :measurementId")
    fun getMeasurementById(measurementId: Int): LiveData<Measurement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("DELETE FROM matavimai WHERE id = :measurementId")
    suspend fun deleteMeasurementById(measurementId: Int)

    @Query("DELETE FROM matavimai")
    suspend fun deleteAllMeasurements()
}
