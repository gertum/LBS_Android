package com.example.os_app_gertum1.ui.useridentification

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.os_app_gertum1.R

class UserIdentificationFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_user_identification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editMac = view.findViewById<EditText>(R.id.edit_mac)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        sharedPreferences = requireContext().getSharedPreferences("AppPrefs", AppCompatActivity.MODE_PRIVATE)

        // Log to check if views are being initialized
        Log.d("UserIdentificationFragment", "editMac: $editMac, btnSave: $btnSave")

        btnSave.setOnClickListener {
            val macAddress = editMac.text.toString()

            if (macAddress.isNotBlank()) {
                Log.d("UserIdentificationFragment", "Saving MAC Address: $macAddress")
                sharedPreferences.edit().putString("MAC_ADDRESS", macAddress).apply()
                Toast.makeText(requireContext(), "MAC Address saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter a valid MAC Address!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
