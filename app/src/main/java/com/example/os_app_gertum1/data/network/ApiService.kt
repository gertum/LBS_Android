package com.example.os_app_gertum1.data.network

import com.example.os_app_gertum1.data.database.Measurement
import retrofit2.Response
import retrofit2.http.GET
import com.example.os_app_gertum1.data.database.SignalStrength

interface ApiService {
    @GET("stiprumai")
    suspend fun fetchSignalStrengths(): Response<List<SignalStrength>>

    @GET("matavimai")
    suspend fun fetchMeasurements(): Response<List<Measurement>>
}
