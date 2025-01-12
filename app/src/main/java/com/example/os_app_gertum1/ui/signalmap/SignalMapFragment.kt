package com.example.os_app_gertum1.ui.signalmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.Measurement
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.databinding.FragmentSignalMapBinding
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.MeasurementDao
import com.example.os_app_gertum1.data.database.SignalStrengthDao
import com.example.os_app_gertum1.data.database.UserMeasurementDAO
import com.example.os_app_gertum1.ui.signalmap.VisitOrderAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignalMapFragment : Fragment(R.layout.fragment_signal_map) {

    private lateinit var binding: FragmentSignalMapBinding
    private lateinit var measurementDao: MeasurementDao
    private lateinit var signalStrengthDao: SignalStrengthDao
    private lateinit var userMeasurementDao: UserMeasurementDAO

    private lateinit var measurementsLiveData: LiveData<List<Measurement>>
    private lateinit var signalStrengthsLiveData: LiveData<List<SignalStrength>>
    private lateinit var userMeasurementsLiveData: LiveData<List<UserMeasurement>>

    private lateinit var adapter: VisitOrderAdapter // RecyclerView Adapter for visit order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalMapBinding.inflate(inflater, container, false)

        // Initialize the database DAOs
        val db = AppDatabase.getDatabase(requireContext())
        measurementDao = db.measurementDao()
        signalStrengthDao = db.signalStrengthDao()
        userMeasurementDao = db.userMeasurementDao()

        // Set up RecyclerView Adapter for visit order
        adapter = VisitOrderAdapter()
        binding.visitOrderRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.visitOrderRecyclerView.adapter = adapter

        // Observe LiveData from DAOs
        measurementsLiveData = measurementDao.getAllMeasurements()
        signalStrengthsLiveData = signalStrengthDao.getAllSignalStrengths()
        userMeasurementsLiveData = userMeasurementDao.getAllMeasurements()

        measurementsLiveData.observe(viewLifecycleOwner, Observer { measurements ->
            signalStrengthsLiveData.observe(viewLifecycleOwner, Observer { signalStrengths ->
                userMeasurementsLiveData.observe(viewLifecycleOwner, Observer { userMeasurements ->
                    renderGrid(measurements, signalStrengths, userMeasurements)
                    displayUserVisitOrder(userMeasurements)
                })
            })
        })

        return binding.root
    }

    private fun renderGrid(
        measurements: List<Measurement>,
        signalStrengths: List<SignalStrength>,
        userMeasurements: List<UserMeasurement>
    ) {
        // Create a map of SignalStrengths by Measurement ID for quick lookup
        val signalStrengthMap = signalStrengths.groupBy { it.measurement }

        // Create a set of all unique x and y values
        val xValues = measurements.map { it.x }.distinct().sorted()
        val yValues = measurements.map { it.y }.distinct().sorted()

        // Collect IDs of measurements closest to user signals
        val closestGridPoints = userMeasurements.mapNotNull { it.closestGridPointId }.toSet()

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
                val cellColor = when {
                    measurement == null -> "gray" // No measurement at this grid point
                    closestGridPoints.contains(measurement.id) -> "purple" // Closest grid point to user signal
                    hasSignal -> "green" // Measurement has signals
                    else -> "red" // Measurement has no signals
                }
                val measurementText = measurement?.id?.let { "m: ${measurement.id}" } ?: ""

                tableHtml.append("""
                    <td style="background-color: $cellColor; color: white; text-align: center;">
                        ${if (measurement != null) "1" else "0"}<br>
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

    private fun displayUserVisitOrder(userMeasurements: List<UserMeasurement>) {
        // Sort the user measurements by their ID or any other field that indicates visit order
        val visitOrder = userMeasurements.sortedBy { it.id }

        // Display the visit order in the RecyclerView or TextView
        adapter.updateData(visitOrder)
    }
}
