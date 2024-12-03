package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stiprumai")
data class SignalStrength(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key with auto-increment
    val measurement: Int, // Measurement number or average measurement number
    val sensor: String, // Sensor name as per configuration
    val strength: Int // Signal strength
)
