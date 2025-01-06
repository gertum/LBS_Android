package com.example.os_app_gertum1.data.repository

import android.util.Log
import com.example.os_app_gertum1.data.database.Measurement
import com.example.os_app_gertum1.data.database.MeasurementDao
import com.example.os_app_gertum1.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MeasurementRepository(
    private val measurementApi: ApiService,
    private val measurementDao: MeasurementDao
) {

    // Refresh data from API
    suspend fun refreshMeasurements() {
        withContext(Dispatchers.IO) {
            try {
                Log.d("MeasurementRepository", "Starting API call...")

                // Make API request to fetch measurements
                val response = measurementApi.fetchMeasurements()

                // Log the request URL and response status
                Log.d("MeasurementRepository", "Full URL: ${response.raw().request.url}")
                Log.d("MeasurementRepository", "Response status: ${response.code()}")
                Log.d("MeasurementRepository", "Response headers: ${response.headers()}")

                if (response.isSuccessful) {
                    val measurements = response.body()

                    if (measurements != null) {
                        Log.d("MeasurementRepository", "Successfully fetched measurements: ${measurements.size} items")

                        // Clear old data and insert new measurements into the database
//                        measurementDao.clearMeasurements() // Make sure you implement this in the DAO
                        measurementDao.insertMeasurements(measurements)
                    } else {
                        Log.e("MeasurementRepository", "Response body is null")
                    }
                } else {
                    // Handle the case where the API response was not successful
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("MeasurementRepository", "API call failed. Error body: $errorBody")
                    Log.e("MeasurementRepository", "Error code: ${response.code()}")
                    Log.e("MeasurementRepository", "Error message: ${response.message()}")
                }

            } catch (e: Exception) {
                // Log any unexpected exceptions that might occur during the API call
                Log.e("MeasurementRepository", "Exception occurred: ${e.localizedMessage}", e)
            }
        }
    }
}

