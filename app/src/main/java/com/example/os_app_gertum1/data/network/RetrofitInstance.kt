package com.example.os_app_gertum1.data.network

import android.content.Context
import com.example.os_app_gertum1.data.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.os_app_gertum1.R

object RetrofitInstance {

    // Don't use const val for the BASE_URL since it's not a compile-time constant
    fun getApiService(context: Context): ApiService {
        // Fetch the BASE_URL from resources at runtime
        val BASE_URL = context.getString(R.string.base_path)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
