package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stiprumai")
data class SignalStrength(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Change id type to Long
    val measurement: Int,
    val sensor: String,
    val strength: Int
) : Parcelable
