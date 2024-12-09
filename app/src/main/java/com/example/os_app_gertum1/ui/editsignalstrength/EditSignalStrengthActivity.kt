package com.example.os_app_gertum1.ui.editsignalstrength

import android.os.Build
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

class EditSignalStrengthActivity : AppCompatActivity() {

    private lateinit var viewModel: SignalStrengthViewModel
    private var signalId: Int = 0 // To identify the signal being edited

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_signal_strength)

        // Initialize Retrofit and API Service
        val retrofit = Retrofit.Builder()
            .baseUrl("http://your-remote-service.com/api/")
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

        // Get the signal data from intent extras
        val signal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("signal_data", SignalStrength::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("signal_data")
        }

        signal?.let {
            signalId = it.id
            measurementInput.setText(it.measurement.toString())
            sensorInput.setText(it.sensor)
            strengthInput.setText(it.strength.toString())
        }

        saveButton.setOnClickListener {
            val measurement = measurementInput.text.toString().toIntOrNull()
            val sensor = sensorInput.text.toString()
            val strength = strengthInput.text.toString().toIntOrNull()

            if (measurement != null && sensor.isNotEmpty() && strength != null) {
                val updatedSignal = SignalStrength(id = signalId, measurement = measurement, sensor = sensor, strength = strength)
                viewModel.updateSignal(updatedSignal) // Use ViewModel to update the signal
                finish() // Close activity after saving
            }
        }
    }
}
