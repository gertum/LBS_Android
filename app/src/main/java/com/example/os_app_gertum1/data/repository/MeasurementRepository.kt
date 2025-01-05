package com.example.os_app_gertum1.data.repository

import com.example.os_app_gertum1.data.database.Measurement
import com.example.os_app_gertum1.data.database.MeasurementDao
import com.example.os_app_gertum1.data.network.ApiService

class MeasurementRepository(
    private val measurementApi: ApiService,
    private val measurementDao: MeasurementDao
) {

    suspend fun fetchAndStoreMeasurements() {
        try {
            // Fetch the response from the API
            val response = measurementApi.fetchMeasurements()

            if (response.isSuccessful) {
                // Extract the list of measurements from the response body
                val measurements = response.body() ?: emptyList()
                // Insert measurements into the database
                measurementDao.insertMeasurements(measurements)
            } else {
                // Handle API error (optional)
                throw Exception("API call failed with error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

