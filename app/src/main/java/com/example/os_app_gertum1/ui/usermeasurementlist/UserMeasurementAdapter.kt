package com.example.os_app_gertum1.ui.usermeasurementlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.databinding.ItemUserMeasurementBinding

class UserMeasurementAdapter(private var userMeasurements: List<UserMeasurement>) :
    RecyclerView.Adapter<UserMeasurementAdapter.UserMeasurementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMeasurementViewHolder {
        val binding = ItemUserMeasurementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserMeasurementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserMeasurementViewHolder, position: Int) {
        val userMeasurement = userMeasurements[position]
        holder.bind(userMeasurement)
    }

    override fun getItemCount(): Int = userMeasurements.size

    fun updateData(newList: List<UserMeasurement>) {
        userMeasurements = newList
        notifyDataSetChanged()
    }

    class UserMeasurementViewHolder(private val binding: ItemUserMeasurementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userMeasurement: UserMeasurement) {
            binding.tvMacAddress.text = userMeasurement.userMacAddress
            binding.tvStrengthToAp1.text = userMeasurement.strengthToAp1.toString()
            binding.tvStrengthToAp2.text = userMeasurement.strengthToAp2.toString()
            binding.tvStrengthToAp3.text = userMeasurement.strengthToAp3.toString()
            binding.tvEuclideanDistance.text = userMeasurement.euclideanDistance?.toString() ?: "N/A"
            binding.tvClosestGridPointId.text = userMeasurement.closestGridPointId?.toString() ?: "N/A"
        }
    }
}
