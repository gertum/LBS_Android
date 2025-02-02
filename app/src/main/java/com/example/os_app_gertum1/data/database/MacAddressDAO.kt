package com.example.os_app_gertum1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MacAddressDao {

    @Insert
    suspend fun insertMacAddress(macAddress: MacAddress)

    @Query("SELECT * FROM mac_address_table WHERE id = :id LIMIT 1")
    suspend fun getMacAddressById(id: Int): MacAddress?

    @Query("SELECT * FROM mac_address_table")
    suspend fun getAllMacAddresses(): List<MacAddress>
    // Fetch the most recent MAC address based on ID or timestamp
    @Query("SELECT * FROM mac_address_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestMacAddress(): MacAddress?
}
