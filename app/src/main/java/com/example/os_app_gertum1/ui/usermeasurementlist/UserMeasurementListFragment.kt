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
import com.example.os_app_gertum1.ui.editusermeasurement.EditUserMeasurementActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            onDeleteClick = { measurement ->
                showDeleteConfirmation(measurement)
            },
            onEditClick = { measurement ->
                navigateToEditMeasurement(measurement)
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
        val userMeasurementDao = AppDatabase.getDatabase(requireContext()).userMeasurementDao()
        userMeasurementDao.getAllMeasurements().observe(viewLifecycleOwner) { userMeasurements ->
            adapter.updateData(userMeasurements)
        }
    }
    private fun deleteMeasurement(measurement: UserMeasurement) {
        val userMeasurementDao = AppDatabase.getDatabase(requireContext()).userMeasurementDao()
        lifecycleScope.launch {
            userMeasurementDao.deleteMeasurement(measurement)
        }
    }
    private fun showDeleteConfirmation(measurement: UserMeasurement) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Measurement")
            .setMessage("Are you sure you want to delete this measurement?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Do nothing, just close the dialog
            }
            .setPositiveButton("Delete") { dialog, _ ->
                deleteMeasurement(measurement) // Perform the deletion
                dialog.dismiss()
            }
            .show()
    }


    private fun navigateToEditMeasurement(measurement: UserMeasurement) {
        val intent = Intent(requireContext(), EditUserMeasurementActivity::class.java)
        intent.putExtra("USER_MEASUREMENT_ID", measurement.id) // Pass ID or other identifying info
        startActivity(intent)
    }
}
