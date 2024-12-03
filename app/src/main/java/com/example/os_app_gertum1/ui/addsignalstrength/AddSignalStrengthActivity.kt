package com.example.os_app_gertum1.ui.addsignalstrength

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.database.SignalStrengthDao
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModel
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModelFactory
import androidx.lifecycle.ViewModelProvider

class AddSignalStrengthActivity : AppCompatActivity() {

    private lateinit var viewModel: SignalStrengthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_signal_strength)

        val dao = AppDatabase.getDatabase(applicationContext).signalStrengthDao()
        val factory = SignalStrengthViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(SignalStrengthViewModel::class.java)

        val measurementInput = findViewById<EditText>(R.id.input_signal_measurement)
        val sensorInput = findViewById<EditText>(R.id.input_signal_sensor)
        val strengthInput = findViewById<EditText>(R.id.input_signal_strength)
        val saveButton = findViewById<Button>(R.id.btn_save_signal)

        saveButton.setOnClickListener {
            val measurement = measurementInput.text.toString().toIntOrNull()
            val sensor = sensorInput.text.toString()
            val strength = strengthInput.text.toString().toIntOrNull()

            if (measurement != null && sensor.isNotEmpty() && strength != null) {
                val newSignal = SignalStrength(measurement = measurement, sensor = sensor, strength = strength)
                viewModel.addSignal(newSignal)
                finish() // Close the activity
            }
        }
    }
}
