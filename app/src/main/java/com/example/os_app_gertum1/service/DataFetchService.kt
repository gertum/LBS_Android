package com.example.os_app_gertum1.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.network.ApiService
import com.example.os_app_gertum1.data.repository.MeasurementRepository
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class DataFetchService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(
            NOTIFICATION_ID,
            buildNotification("Fetching data...")
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            try {
                val baseUrl = getString(R.string.base_path)

                // Configure OkHttpClient with custom timeouts and SSL disabled
                val okHttpClient = getUnsafeOkHttpClient()

                // Fetch and cache data
                val database = AppDatabase.getDatabase(applicationContext)

                // Initialize Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)  // Replace with your API base URL
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)
                val measurementRepo = MeasurementRepository(apiService, database.measurementDao())
                val signalStrengthRepo = SignalStrengthRepository(apiService, database.signalStrengthDao())

                while (true) {
                    measurementRepo.refreshMeasurements()
                    signalStrengthRepo.refreshSignalStrengths()

                    Log.d("DataFetchService", "Data fetched successfully.")
                    delay(15 * 60 * 1000) // Repeat every 15 minutes
                }
            } catch (e: Exception) {
                Log.e("DataFetchService", "Error fetching data", e)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Data Fetch Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Data Fetch Service")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Set priority for backward compatibility
            .build()
    }

    companion object {
        const val CHANNEL_ID = "DataFetchServiceChannel"
        const val NOTIFICATION_ID = 1
    }

    // Function to create OkHttpClient with SSL verification disabled
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        // Create a trust manager that does not perform certificate validation
        val trustAllCertificates = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }

        // Install the all-trusting trust manager
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustAllCertificates), SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to create SSL context", e)
        } catch (e: KeyManagementException) {
            throw RuntimeException("Failed to initialize SSL context", e)
        }

        // Set up OkHttpClient to use the custom SSLContext and disable hostname verification
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates)
            .hostnameVerifier { _, _ -> true }  // Disable hostname verification
            .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
            .build()
    }
}
