package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signal_strengths")
data class SignalStrength(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // e.g., WiFi name or identifier
    val strength: Int // Signal strength value, e.g., -70 dBm
)
