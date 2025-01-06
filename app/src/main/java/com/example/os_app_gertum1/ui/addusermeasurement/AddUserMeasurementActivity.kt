package com.example.os_app_gertum1.ui.addusermeasurement

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.database.UserMeasurement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class AddUserMeasurementActivity : AppCompatActivity() {

    private lateinit var editUserMacAddress: EditText
    private lateinit var editStrengthToAp1: EditText
    private lateinit var editStrengthToAp2: EditText
    private lateinit var editStrengthToAp3: EditText
    private lateinit var saveButton: Button
    private lateinit var goBackButton: Button

    private var userMeasurementId: Int? = null // ID for editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_measurement)

        // Initialize the views
        editUserMacAddress = findViewById(R.id.edit_user_mac_address)
        editStrengthToAp1 = findViewById(R.id.edit_strength_to_ap1)
        editStrengthToAp2 = findViewById(R.id.edit_strength_to_ap2)
        editStrengthToAp3 = findViewById(R.id.edit_strength_to_ap3)
        saveButton = findViewById(R.id.button_save_user_measurement)
        goBackButton = findViewById(R.id.btn_go_back)

        // Check if we're editing an existing UserMeasurement
        userMeasurementId = intent.getIntExtra("USER_MEASUREMENT_ID", -1).takeIf { it != -1 }

        if (userMeasurementId != null) {
            loadUserMeasurement(userMeasurementId!!)
        } else {
            prefillMacAddress() // Prefill only for new measurements
        }

        // Set up save button listener
        saveButton.setOnClickListener {
            saveUserMeasurement()
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
                    Toast.makeText(this@AddUserMeasurementActivity, "Measurement not found", Toast.LENGTH_SHORT).show()
                    finish() // Close activity if invalid ID
                }
            }
        }
    }

    private fun saveUserMeasurement() {
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
                val (closestGridPointId, distance) = findClosestMeasurement(
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
                        userMacAddress = userMacAddress,
                        strengthToAp1 = strengthToAp1,
                        strengthToAp2 = strengthToAp2,
                        strengthToAp3 = strengthToAp3,
                        closestGridPointId = closestGridPointId,
                        euclideanDistance = distance
                    )
                    userMeasurementDao.insertMeasurement(newMeasurement)

                    launch(Dispatchers.Main) {
                        Toast.makeText(
                            this@AddUserMeasurementActivity,
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

    private fun findClosestMeasurement(
        userStrengths: List<Pair<String, Int>>,
        signalStrengths: List<SignalStrength>
    ): Pair<Int, Double> {
        var closestGridPointId = -1
        var minDistance = Double.MAX_VALUE

        val groupedSignalStrengths = signalStrengths.groupBy { it.measurement }

        for ((measurementId, strengths) in groupedSignalStrengths) {
            val strengthsMap = strengths.associateBy({ it.sensor }, { it.strength })

            val distance = computeDistance(userStrengths, strengthsMap)

            if (distance < minDistance) {
                minDistance = distance
                closestGridPointId = measurementId
            }
        }

        return Pair(closestGridPointId, minDistance)
    }

    private fun computeDistance(
        userStrengths: List<Pair<String, Int>>,
        strengthsMap: Map<String, Int?>
    ): Double {
        return sqrt(
            userStrengths.sumOf { (sensor, strength) ->
                val difference = strength - (strengthsMap[sensor] ?: 0)
                difference * difference.toDouble()
            }
        )
    }
}
