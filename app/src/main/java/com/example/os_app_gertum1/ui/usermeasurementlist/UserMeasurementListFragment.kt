package com.example.os_app_gertum1.ui.usermeasurementlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.ui.addusermeasurement.AddUserMeasurementActivity
import kotlinx.coroutines.launch
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserMeasurementListFragment : Fragment() {

    private lateinit var adapter: UserMeasurementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_measurement_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_user_measurements)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UserMeasurementAdapter(
            emptyList(),
            onItemClick = { measurement ->
                // Handle item click if needed
            },
            onEditClick = { measurement ->
                navigateToEditMeasurement(measurement) // Navigate to Edit
            }
        )
        recyclerView.adapter = adapter

        val fabAddMeasurement = view.findViewById<FloatingActionButton>(R.id.fab_add_measurement)
        fabAddMeasurement.setOnClickListener {
            val intent = Intent(requireContext(), AddUserMeasurementActivity::class.java)
            startActivity(intent)
        }

        loadUserMeasurements()
    }

    private fun loadUserMeasurements() {
        lifecycleScope.launch {
            val userMeasurements = AppDatabase.getDatabase(requireContext()).userMeasurementDao().getAllMeasurements()
            adapter.updateData(userMeasurements)
        }
    }

    private fun navigateToEditMeasurement(measurement: UserMeasurement) {
        val intent = Intent(requireContext(), AddUserMeasurementActivity::class.java)
        intent.putExtra("USER_MEASUREMENT_ID", measurement.id) // Pass ID or other identifying info
        startActivity(intent)
    }
}
