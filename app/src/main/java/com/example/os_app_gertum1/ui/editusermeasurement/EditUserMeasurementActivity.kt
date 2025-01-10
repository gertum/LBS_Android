package com.example.os_app_gertum1.ui.editusermeasurement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.service.DistanceCalculationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUserMeasurementActivity : AppCompatActivity() {

    private lateinit var editUserMacAddress: EditText
    private lateinit var editStrengthToAp1: EditText
    private lateinit var editStrengthToAp2: EditText
    private lateinit var editStrengthToAp3: EditText
    private lateinit var saveButton: Button
    private lateinit var goBackButton: Button

    private var userMeasurementId: Int = -1 // ID for editing
    private val distanceService = DistanceCalculationService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_measurement)

        // Initialize the views
        editUserMacAddress = findViewById(R.id.edit_user_mac_address)
        editStrengthToAp1 = findViewById(R.id.edit_strength_to_ap1)
        editStrengthToAp2 = findViewById(R.id.edit_strength_to_ap2)
        editStrengthToAp3 = findViewById(R.id.edit_strength_to_ap3)
        saveButton = findViewById(R.id.button_save_user_measurement)
        goBackButton = findViewById(R.id.btn_go_back)

        // Initialize the userMeasurementId from the Intent
        userMeasurementId = intent.getIntExtra("USER_MEASUREMENT_ID", -1).also {
            require(it != -1) { "USER_MEASUREMENT_ID is missing or invalid in Intent" }
        }
        // Call this method to prefill the fields with existing measurement data
        loadUserMeasurement(userMeasurementId)

        // Set up save button listener
        saveButton.setOnClickListener {
            saveUserMeasurement(userMeasurementId)
        }
        goBackButton.setOnClickListener {
            finish()
        }
    }

    private fun prefillMacAddress() {
        CoroutineScope(Dispatchers.IO).launch {
            val macAddressDao = AppDatabase.getDatabase(applicationContext).macAddressDao()
            val lastMacAddress = macAddressDao.getLatestMacAddress()

            // Switch to the main thread to update the UI
            launch(Dispatchers.Main) {
                if (lastMacAddress != null) {
                    editUserMacAddress.setText(lastMacAddress.macAddress)
                } else {
                    editUserMacAddress.hint = "Please enter MAC Address in the User tab first"
                }
            }
        }
    }

    private fun loadUserMeasurement(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val userMeasurementDao = AppDatabase.getDatabase(applicationContext).userMeasurementDao()
            val measurement = userMeasurementDao.getMeasurementById(id)

            if (measurement != null) {
                launch(Dispatchers.Main) {
                    editUserMacAddress.setText(measurement.userMacAddress)
                    editStrengthToAp1.setText(measurement.strengthToAp1.toString())
                    editStrengthToAp2.setText(measurement.strengthToAp2.toString())
                    editStrengthToAp3.setText(measurement.strengthToAp3.toString())
                }
            } else {
                launch(Dispatchers.Main) {
                    Toast.makeText(this@EditUserMeasurementActivity, "Measurement not found", Toast.LENGTH_SHORT).show()
                    finish() // Close activity if invalid ID
                }
            }
        }
    }

    private fun saveUserMeasurement(id: Int) {
        val userMacAddress = editUserMacAddress.text.toString()
        val strengthToAp1 = editStrengthToAp1.text.toString().toIntOrNull()
        val strengthToAp2 = editStrengthToAp2.text.toString().toIntOrNull()
        val strengthToAp3 = editStrengthToAp3.text.toString().toIntOrNull()

        if (userMacAddress.isBlank() || strengthToAp1 == null || strengthToAp2 == null || strengthToAp3 == null) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val db = AppDatabase.getDatabase(applicationContext)
        val signalStrengthDao = db.signalStrengthDao()

        // Observe LiveData and process the signal strengths
        signalStrengthDao.getAllSignalStrengths().observe(this) { allSignalStrengths ->
            if (!allSignalStrengths.isNullOrEmpty()) {
                val (closestGridPointId, distance) = distanceService.findClosestMeasurement(
                    userStrengths = listOf(
                        "wiliboxas1" to strengthToAp1,
                        "wiliboxas2" to strengthToAp2,
                        "wiliboxas3" to strengthToAp3
                    ),
                    signalStrengths = allSignalStrengths
                )

                // Save the user measurement with closest grid point ID and distance
                CoroutineScope(Dispatchers.IO).launch {
                    val userMeasurementDao = db.userMeasurementDao()

                    val newMeasurement = UserMeasurement(
                        userMeasurementId,
                        userMacAddress = userMacAddress,
                        strengthToAp1 = strengthToAp1,
                        strengthToAp2 = strengthToAp2,
                        strengthToAp3 = strengthToAp3,
                        closestGridPointId = closestGridPointId,
                        euclideanDistance = distance
                    )
                    userMeasurementDao.updateMeasurement(newMeasurement)

                    launch(Dispatchers.Main) {
                        Toast.makeText(
                            this@EditUserMeasurementActivity,
                            "Measurement saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "No signal strengths available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
