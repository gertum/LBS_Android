package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matavimai")
data class Measurement(
    @PrimaryKey val id: Int, // Corresponds to `matavimas` in SQL
    val x: Int,
    val y: Int,
    val distance: Float // Corresponds to `atstumas` in SQL
)
