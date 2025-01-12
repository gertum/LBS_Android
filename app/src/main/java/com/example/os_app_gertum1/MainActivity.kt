package com.example.os_app_gertum1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.os_app_gertum1.service.DataFetchService
import com.example.os_app_gertum1.utils.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch and store data on first run
        if (PreferenceManager.isFirstRun(this)) {
            val serviceIntent = Intent(this, DataFetchService::class.java)
            startService(serviceIntent)
            PreferenceManager.setFirstRun(this, false)
        }


        // Set up NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Link BottomNavigationView with NavController
        bottomNavigation.setupWithNavController(navController)
    }
}
