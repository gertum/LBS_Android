package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stiprumai")
data class SignalStrength(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("matavimas") val measurement: Int,
    @SerializedName("sensorius") val sensor: String,
    @SerializedName("stiprumas") val strength: Int
) : Parcelable
