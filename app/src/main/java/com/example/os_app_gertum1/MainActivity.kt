package com.example.os_app_gertum1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.network.ApiService
import com.example.os_app_gertum1.data.repository.MeasurementRepository
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository
import com.example.os_app_gertum1.ui.SignalMapFragment
import com.example.os_app_gertum1.ui.map.MapFragment
import com.example.os_app_gertum1.ui.signalstrengthlist.SignalStrengthListFragment
import com.example.os_app_gertum1.ui.useridentification.UserIdentificationFragment
import com.example.os_app_gertum1.ui.usermeasurementlist.UserMeasurementListFragment
import com.example.os_app_gertum1.utils.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = getString(R.string.base_path)

        // Configure OkHttpClient with custom timeouts and SSL disabled
        val okHttpClient = getUnsafeOkHttpClient()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)  // Replace with your API base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Initialize Room Database
        val database = AppDatabase.getDatabase(this)

        // DAOs
        val measurementDao = database.measurementDao()
        val signalStrengthDao = database.signalStrengthDao()

        // Repositories
        val measurementRepository = MeasurementRepository(apiService, measurementDao)
        val signalStrengthRepository = SignalStrengthRepository(apiService, signalStrengthDao)

        // Fetch and store data on first run
        if (PreferenceManager.isFirstRun(this)) {
            lifecycleScope.launch {
                try {
                    // Fetch and store Measurements
                    measurementRepository.refreshMeasurements()

                    // Fetch and store SignalStrengths
                    signalStrengthRepository.refreshSignalStrengths()

                    // Mark first run as completed
                    PreferenceManager.setFirstRun(this@MainActivity, false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Set up navigation item selection
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SignalMapFragment())
                        .commit()
                    true
                }

                R.id.navigation_signal_list -> {
                    // Replace fragment with SignalStrengthListFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, UserMeasurementListFragment())
                        .commit()
                    true
                }
                R.id.navigation_user -> {
                    // Replace fragment with UserIdentificationFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, UserIdentificationFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Load default fragment (e.g., MapFragment)
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.nav_map
        }
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

