package com.example.os_app_gertum1.ui.useridentification

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.os_app_gertum1.R

class UserIdentificationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_identification)

        val editMac = findViewById<EditText>(R.id.edit_mac)
        val btnSave = findViewById<Button>(R.id.btn_save)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        // Log to check if views are being initialized
        Log.d("UserIdentificationActivity", "editMac: $editMac, btnSave: $btnSave")

        btnSave.setOnClickListener {
            val macAddress = editMac.text.toString()

            if (macAddress.isNotBlank()) {
                Log.d("UserIdentificationActivity", "Saving MAC Address: $macAddress")
                sharedPreferences.edit().putString("MAC_ADDRESS", macAddress).apply()
                Toast.makeText(this, "MAC Address saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid MAC Address!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

