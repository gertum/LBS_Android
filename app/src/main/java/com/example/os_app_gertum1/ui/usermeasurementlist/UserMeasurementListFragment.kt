package com.example.os_app_gertum1.ui.usermeasurementlist

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
import kotlinx.coroutines.launch

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

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_user_measurements)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up adapter with an empty list to start
        adapter = UserMeasurementAdapter(emptyList())
        recyclerView.adapter = adapter

        // Load UserMeasurement data from the database
        loadUserMeasurements()
    }

    private fun loadUserMeasurements() {
        lifecycleScope.launch {
            // Fetch all UserMeasurement records from Room DB
            val userMeasurements = AppDatabase.getDatabase(requireContext()).userMeasurementDao().getAllMeasurements()
            // Update RecyclerView with the fetched data
            adapter.updateData(userMeasurements)
        }
    }
}
