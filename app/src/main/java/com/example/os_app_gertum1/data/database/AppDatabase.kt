package com.example.os_app_gertum1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SignalStrength::class, User::class, Measurement::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Provide DAOs for accessing the data
    abstract fun signalStrengthDao(): SignalStrengthDao
    abstract fun userDao(): UserDao
    abstract fun measurementDao(): MeasurementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Replace with your desired database name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
