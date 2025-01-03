package com.example.os_app_gertum1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mac_address_table")
data class MacAddress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Optional auto-generated ID
    val macAddress: String
)
