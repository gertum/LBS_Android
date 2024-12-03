package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vartotojai")
data class User(
    @PrimaryKey val id: Int,
    val mac: String,
    val sensor: String, // Corresponds to `sensorius` in SQL
    val strength: Int // Corresponds to `stiprumas` in SQL
)