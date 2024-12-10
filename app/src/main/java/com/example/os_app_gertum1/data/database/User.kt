package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "vartotojai")
data class User(
    @PrimaryKey val id: Int,
    val mac: String,
    @SerializedName("sensorius") val sensor: String, // Corresponds to `sensorius` in SQL
    @SerializedName("stiprumas") val strength: Int // Corresponds to `stiprumas` in SQL
)