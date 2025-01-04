package com.example.os_app_gertum1.ui.addusermeasurement

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.UserMeasurement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddUserMeasurementActivity : AppCompatActivity() {

    private lateinit var editUserMacAddress: EditText
    private lateinit var editStrengthToAp1: EditText
    private lateinit var editStrengthToAp2: EditText
    private lateinit var editStrengthToAp3: EditText
    private lateinit var editEuclideanDistance: EditText
    private lateinit var editClosestGridPointId: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_measurement)

        // Initialize the views
        editUserMacAddress = findViewById(R.id.edit_user_mac_address)
        editStrengthToAp1 = findViewById(R.id.edit_strength_to_ap1)
        editStrengthToAp2 = findViewById(R.id.edit_strength_to_ap2)
        editStrengthToAp3 = findViewById(R.id.edit_strength_to_ap3)
        saveButton = findViewById(R.id.button_save_user_measurement)

        // Set up save button listener
        saveButton.setOnClickListener {
            saveUserMeasurement()
        }
    }

    // Function to save the UserMeasurement to the database
    private fun saveUserMeasurement() {
        // Get the values from the EditText fields
        val userMacAddress = editUserMacAddress.text.toString()
        val strengthToAp1 = editStrengthToAp1.text.toString().toIntOrNull()
        val strengthToAp2 = editStrengthToAp2.text.toString().toIntOrNull()
        val strengthToAp3 = editStrengthToAp3.text.toString().toIntOrNull()

        // Validate input
        if (userMacAddress.isEmpty() || strengthToAp1 == null || strengthToAp2 == null || strengthToAp3 == null) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new UserMeasurement object
        val userMeasurement = UserMeasurement(
            userMacAddress = userMacAddress,
            strengthToAp1 = strengthToAp1,
            strengthToAp2 = strengthToAp2,
            strengthToAp3 = strengthToAp3,
            euclideanDistance = null,
            closestGridPointId = null
        )

        // Save the UserMeasurement asynchronously in the database
        CoroutineScope(Dispatchers.IO).launch {
            val userMeasurementDao = AppDatabase.getDatabase(applicationContext).userMeasurementDao()
            userMeasurementDao.insertMeasurement(userMeasurement)

            // Show a success message on the main thread
            launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "User Measurement saved successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after saving
            }
        }
    }
}
