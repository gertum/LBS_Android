package com.example.os_app_gertum1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SignalStrength::class, User::class, Measurement::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Provide DAOs for accessing the data
    abstract fun signalStrengthDao(): SignalStrengthDao
    abstract fun userDao(): UserDao
    abstract fun measurementDao(): MeasurementDao
}

