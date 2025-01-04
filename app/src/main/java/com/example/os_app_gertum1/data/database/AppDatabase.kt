package com.example.os_app_gertum1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SignalStrength::class, User::class, Measurement::class, MacAddress::class, UserMeasurement::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Provide DAOs for accessing the data
    abstract fun signalStrengthDao(): SignalStrengthDao
    abstract fun userDao(): UserDao
    abstract fun measurementDao(): MeasurementDao

    // New DAOs for the actual app
    abstract fun macAddressDao(): MacAddressDao
    abstract fun userMeasurementDao(): UserMeasurementDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Replace with your desired database name
                )
//                    .fallbackToDestructiveMigration() //might cause something bad later idk
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
