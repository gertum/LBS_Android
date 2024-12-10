package com.example.os_app_gertum1.ui.addsignalstrength

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.network.ApiService
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModel
import com.example.os_app_gertum1.viewmodel.SignalStrengthViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddSignalStrengthActivity : AppCompatActivity() {

    private lateinit var viewModel: SignalStrengthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_signal_strength)

        val baseUrl = getString(R.string.base_path)

        // Initialize Retrofit and API Service
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        // Initialize database, DAO, repository, and ViewModel
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = SignalStrengthRepository(apiService, database.signalStrengthDao())
        val factory = SignalStrengthViewModelFactory(repository)
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
                val newSignal = SignalStrength(
                    id = 0, // Room will auto-generate the ID
                    measurement = measurement,
                    sensor = sensor,
                    strength = strength
                )
                viewModel.addSignal(newSignal) // Add the signal using ViewModel
                finish() // Close the activity
            }
        }
    }
}
