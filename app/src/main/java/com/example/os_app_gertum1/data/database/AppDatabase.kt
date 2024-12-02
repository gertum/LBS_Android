package com.example.os_app_gertum1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SignalStrength::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun signalStrengthDao(): SignalStrengthDao
}
