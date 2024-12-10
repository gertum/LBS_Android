package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "matavimai")
data class Measurement(
    @PrimaryKey  @SerializedName("matavimas") val id: Int,
    val x: Int,
    val y: Int,
    @SerializedName("atstumas") val distance: Float
)
