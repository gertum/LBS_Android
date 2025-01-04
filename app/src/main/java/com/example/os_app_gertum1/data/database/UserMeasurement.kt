package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_measurement_table",
    foreignKeys = [
        ForeignKey(
            entity = Measurement::class, // Reference the parent entity
            parentColumns = ["id"],     // Column in the parent entity
            childColumns = ["closestGridPointId"], // Column in the child entity
            onDelete = ForeignKey.CASCADE // Optional: Define behavior on delete
        )
    ]
)
data class UserMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userMacAddress: String,
    val strengthToAp1: Int,
    val strengthToAp2: Int,
    val strengthToAp3: Int,
    val euclideanDistance: Double?,
    val closestGridPointId: Int? // Foreign key reference to Measurement.id
)
