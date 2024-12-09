package com.example.os_app_gertum1.data.network

import retrofit2.Response
import retrofit2.http.GET
import com.example.os_app_gertum1.data.database.SignalStrength

interface ApiService {
    @GET("signal_strengths")
    suspend fun fetchSignalStrengths(): Response<List<SignalStrength>> // Wrap in Response
}
