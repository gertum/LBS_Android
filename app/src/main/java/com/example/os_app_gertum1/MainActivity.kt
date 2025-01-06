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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = getString(R.string.base_path)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)  // Replace with your API base URL
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
}

