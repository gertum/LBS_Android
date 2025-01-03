package com.example.os_app_gertum1.ui.useridentification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.MacAddress
import kotlinx.coroutines.launch

class UserIdentificationFragment : Fragment() {

    private lateinit var editMac: EditText
    private lateinit var btnSave: Button
    private lateinit var currentMacText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_identification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editMac = view.findViewById(R.id.edit_mac)
        btnSave = view.findViewById(R.id.btn_save)
        currentMacText = view.findViewById(R.id.tv_current_mac) // TextView to display current MAC

        // Display the current MAC address from Room DB
        displayCurrentMac()

        btnSave.setOnClickListener {
            val macAddress = editMac.text.toString()

            if (macAddress.isNotBlank()) {
                saveMacAddress(macAddress)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid MAC Address!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayCurrentMac() {
        lifecycleScope.launch {
            val macAddress = AppDatabase.getDatabase(requireContext()).macAddressDao().getMacAddressById(1)
            macAddress?.let {
                currentMacText.text = "Current MAC: ${it.macAddress}"
            } ?: run {
                currentMacText.text = "No MAC Address saved."
            }
        }
    }

    private fun saveMacAddress(macAddress: String) {
        lifecycleScope.launch {
            // Save the MAC address to the Room DB
            val newMac = MacAddress(macAddress = macAddress)
            AppDatabase.getDatabase(requireContext()).macAddressDao().insertMacAddress(newMac)
            Toast.makeText(requireContext(), "MAC Address saved!", Toast.LENGTH_SHORT).show()
            displayCurrentMac() // Refresh the displayed MAC address
        }
    }
}
