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
            try {
                Log.d("SignalRepository", "Starting API call...")

                // Make API request
                val response = api.fetchSignalStrengths()

                // Log the full URL of the request using the raw response
                Log.d("SignalRepository", "Full URL: ${response.raw().request.url}")

                // Log the response status and headers
                Log.d("SignalRepository", "Response status: ${response.code()}")
                Log.d("SignalRepository", "Response headers: ${response.headers()}")

                if (response.isSuccessful) {
                    val signalStrengths = response.body()

                    if (signalStrengths != null) {
                        Log.d("SignalRepository", "Successfully fetched signal strengths: ${signalStrengths.size} items")

                        // Clear and insert new data
                        signalStrengthDao.clearSignalStrengths()
                        signalStrengthDao.insertAllSignalStrengths(signalStrengths)
                    } else {
                        Log.e("SignalRepository", "Response body is null")
                    }
                } else {
                    // Log the error response body
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("SignalRepository", "API call failed. Error body: $errorBody")
                    Log.e("SignalRepository", "Error code: ${response.code()}")
                    Log.e("SignalRepository", "Error message: ${response.message()}")
                }

            } catch (e: Exception) {
                // Log any unexpected exceptions that might occur
                Log.e("SignalRepository", "Exception occurred: ${e.localizedMessage}", e)
            }
        }
    }

//    // Retrieve data from local database
//    fun getSignalStrengthsFromDb(): LiveData<List<SignalStrength>> = signalStrengthDao.getAllSignalStrengths()
    fun getSignalStrengthsFromDb(): LiveData<List<SignalStrength>> {
        return signalStrengthDao.getAllSignalStrengths()
    }

    //bad TODO remove
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
