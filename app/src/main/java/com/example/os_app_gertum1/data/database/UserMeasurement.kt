package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_measurement_table",
    foreignKeys = [
        ForeignKey(
            entity = Measurement::class,     // Reference to the Measurement entity
            parentColumns = ["matavimas"],    // Column in Measurement entity (primary key)
            childColumns = ["closestGridPointId"], // Column in UserMeasurement (foreign key)
            onDelete = ForeignKey.CASCADE     // Cascade delete behavior if Measurement is deleted
        )
    ]
)
data class UserMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userMacAddress: String,
    val strengthToAp1: Int,
    val strengthToAp2: Int,
    val strengthToAp3: Int,
    val euclideanDistance: Double?, // Nullable
    val closestGridPointId: Int?    // Foreign key referencing matavimas from Measurement
)

