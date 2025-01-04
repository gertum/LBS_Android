package com.example.os_app_gertum1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.os_app_gertum1.ui.map.MapFragment
import com.example.os_app_gertum1.ui.signalstrengthlist.SignalStrengthListFragment
import com.example.os_app_gertum1.ui.useridentification.UserIdentificationFragment
import com.example.os_app_gertum1.ui.usermeasurementlist.UserMeasurementListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Set up navigation item selection
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MapFragment())
                        .commit()
                    true
                }

                //yeah maybe later;; seems good
//                R.id.navigation_signal_list -> {
//                    // Replace fragment with SignalStrengthListFragment
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, SignalStrengthListFragment())
//                        .commit()
//                    true
//                }
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

