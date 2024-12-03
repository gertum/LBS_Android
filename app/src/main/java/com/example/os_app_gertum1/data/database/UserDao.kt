package com.example.os_app_gertum1.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM vartotojai")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM vartotojai WHERE mac = :macAddress")
    fun getUserByMac(macAddress: String): LiveData<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM vartotojai WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("DELETE FROM vartotojai")
    suspend fun deleteAllUsers()
}
