package com.example.os_app_gertum1.data.repository


import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.SignalStrength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//class SignalStrengthRepository(private val database: AppDatabase) {
//
//    // Insert a SignalStrength object into the database
//    suspend fun insert(signalStrength: SignalStrength) {
//        withContext(Dispatchers.IO) {
//            database.signalStrengthDao().insert(signalStrength)
//        }
//    }
//
//    // Get all SignalStrength objects from the database
//    suspend fun getAll(): List<SignalStrength> {
//        return withContext(Dispatchers.IO) {
//            database.signalStrengthDao().getAll()
//        }
//    }
//
//    // Get SignalStrength by ID
//    suspend fun getById(id: Int): SignalStrength? {
//        return withContext(Dispatchers.IO) {
//            database.signalStrengthDao().getById(id)
//        }
//    }
//}
