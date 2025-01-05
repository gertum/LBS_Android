package com.example.os_app_gertum1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.Measurement
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.databinding.FragmentSignalMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.MeasurementDao
import com.example.os_app_gertum1.data.database.SignalStrengthDao

class SignalMapFragment : Fragment(R.layout.fragment_signal_map) {

    private lateinit var binding: FragmentSignalMapBinding
    private lateinit var measurementDao: MeasurementDao
    private lateinit var signalStrengthDao: SignalStrengthDao

    // Variables to hold live data
    private lateinit var measurementsLiveData: LiveData<List<Measurement>>
    private lateinit var signalStrengthsLiveData: LiveData<List<SignalStrength>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalMapBinding.inflate(inflater, container, false)

        // Get the database instance and initialize the DAOs
        val db = AppDatabase.getDatabase(requireContext())
        measurementDao = db.measurementDao()
        signalStrengthDao = db.signalStrengthDao()

        // Get the LiveData from DAOs
        measurementsLiveData = measurementDao.getAllMeasurements() // LiveData
        signalStrengthsLiveData = signalStrengthDao.getAllSignalStrengths() // LiveData

        // Observe the LiveData
        measurementsLiveData.observe(viewLifecycleOwner, Observer { measurements ->
            signalStrengthsLiveData.observe(viewLifecycleOwner, Observer { signalStrengths ->
                // Once both LiveData are updated, render the grid
                renderGrid(measurements, signalStrengths)
            })
        })

        return binding.root
    }

    private fun renderGrid(measurements: List<Measurement>, signalStrengths: List<SignalStrength>) {
        // Create a map of SignalStrengths by Measurement ID for quick lookup
        val signalStrengthMap = signalStrengths.groupBy { it.measurement }

        // Create a set of all unique x and y values
        val xValues = measurements.map { it.x }.distinct().sorted()
        val yValues = measurements.map { it.y }.distinct().sorted()

        // Build the table dynamically
        val tableHtml = StringBuilder()

        // Header Row: X-axis labels
        tableHtml.append("<table border='1' style='border-collapse: collapse;'>")
        tableHtml.append("<thead><tr><th></th>")
        xValues.forEach { x ->
            tableHtml.append("<th>$x</th>")
        }
        tableHtml.append("</tr></thead><tbody>")

        // Data Rows: Y-axis labels
        yValues.forEach { y ->
            tableHtml.append("<tr><th>$y</th>")

            xValues.forEach { x ->
                // Find if there's a measurement at (x, y)
                val measurement = measurements.find { it.x == x && it.y == y }
                val hasSignal = measurement?.let { signalStrengthMap[it.id]?.isNotEmpty() } ?: false

                // Determine cell color
                val cellColor = if (hasSignal) "green" else "red"
                val measurementText = measurement?.id?.let { "m: ${measurement.id}" } ?: ""

                tableHtml.append("""
                    <td style="background-color: $cellColor; color: white; text-align: center;">
                        ${if (hasSignal) "1" else "0"}<br>
                        <span style="font-size: 10px; color: black;">$measurementText</span>
                    </td>
                """)
            }

            tableHtml.append("</tr>")
        }

        tableHtml.append("</tbody></table>")

        // Update the UI with the table
        binding.gridContainer.loadDataWithBaseURL(null, tableHtml.toString(), "text/html", "UTF-8", null)
    }
}