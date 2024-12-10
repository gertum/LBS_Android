package com.example.os_app_gertum1.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.database.SignalStrengthDao
import com.example.os_app_gertum1.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignalStrengthRepository(
    private val api: ApiService,
    private val signalStrengthDao: SignalStrengthDao
) {

    // Refresh data from API
    suspend fun refreshSignalStrengths() {
        withContext(Dispatchers.IO) {
            val response = api.fetchSignalStrengths()
            if (response.isSuccessful) {
                val signalStrengths = response.body()
                if (signalStrengths != null) {
                    signalStrengthDao.clearSignalStrengths() // Clear existing data
                    signalStrengthDao.insertAllSignalStrengths(signalStrengths) // Insert new data
                } else {
                    Log.e("SignalRepository", "Response body is null")
                }
            } else {
                Log.e("SignalRepository", "API call failed: ${response.errorBody()}")
            }
        }
    }

//    // Retrieve data from local database
//    fun getSignalStrengthsFromDb(): LiveData<List<SignalStrength>> = signalStrengthDao.getAllSignalStrengths()
    fun getSignalStrengthsFromDb(): LiveData<List<SignalStrength>> {
        return signalStrengthDao.getAllSignalStrengths()
    }

    // Add a new signal strength
    suspend fun addSignal(signal: SignalStrength) {
        signalStrengthDao.insertSignalStrength(signal)
    }

    // Update an existing signal strength
    suspend fun updateSignal(signal: SignalStrength) {
        signalStrengthDao.updateSignalStrength(signal)
    }

    // Delete a signal strength
    suspend fun deleteSignal(signal: SignalStrength) {
        signalStrengthDao.deleteSignalStrength(signal)
    }
}
