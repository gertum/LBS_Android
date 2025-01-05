package com.example.os_app_gertum1.ui.usermeasurementlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.databinding.ItemUserMeasurementBinding

class UserMeasurementAdapter(
    private var measurements: List<UserMeasurement>,
    private val onItemClick: (UserMeasurement) -> Unit,
    private val onEditClick: (UserMeasurement) -> Unit // New callback for edit
) : RecyclerView.Adapter<UserMeasurementAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMacAddress = itemView.findViewById<TextView>(R.id.tv_mac_address)
        private val tvStrengthToAp1 = itemView.findViewById<TextView>(R.id.tv_strength_to_ap1)
        private val tvStrengthToAp2 = itemView.findViewById<TextView>(R.id.tv_strength_to_ap2)
        private val tvStrengthToAp3 = itemView.findViewById<TextView>(R.id.tv_strength_to_ap3)
        private val tvEuclideanDistance = itemView.findViewById<TextView>(R.id.tv_euclidean_distance)
        private val tvClosestGridPointId = itemView.findViewById<TextView>(R.id.tv_closest_grid_point_id)
        private val btnEditMeasurement = itemView.findViewById<Button>(R.id.btn_edit_measurement)

        fun bind(measurement: UserMeasurement) {
            tvMacAddress.text = "MAC Address: ${measurement.userMacAddress}"
            tvStrengthToAp1.text = "Strength To AP1: ${measurement.strengthToAp1}"
            tvStrengthToAp2.text = "Strength To AP2: ${measurement.strengthToAp2}"
            tvStrengthToAp3.text = "Strength To AP3: ${measurement.strengthToAp3}"
            tvEuclideanDistance.text = "Euclidean Distance: ${measurement.euclideanDistance ?: "N/A"}"
            tvClosestGridPointId.text = "Closest Grid Point ID: ${measurement.closestGridPointId ?: "N/A"}"

            // Handle Edit Button Click
            btnEditMeasurement.setOnClickListener {
                onEditClick(measurement) // Trigger the edit callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_measurement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(measurements[position])
    }

    override fun getItemCount(): Int = measurements.size

    fun updateData(newMeasurements: List<UserMeasurement>) {
        measurements = newMeasurements
        notifyDataSetChanged()
    }
}
