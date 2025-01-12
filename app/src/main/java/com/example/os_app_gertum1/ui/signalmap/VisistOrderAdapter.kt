package com.example.os_app_gertum1.ui.signalmap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.data.database.UserMeasurement
import com.example.os_app_gertum1.databinding.ItemVisitOrderBinding

class VisitOrderAdapter : RecyclerView.Adapter<VisitOrderAdapter.VisitOrderViewHolder>() {

    private var visitOrderList: List<UserMeasurement> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitOrderViewHolder {
        val binding = ItemVisitOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisitOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitOrderViewHolder, position: Int) {
        val userMeasurement = visitOrderList[position]
        holder.bind(userMeasurement)
    }

    override fun getItemCount(): Int {
        return visitOrderList.size
    }

    fun updateData(newData: List<UserMeasurement>) {
        visitOrderList = newData
        notifyDataSetChanged()
    }

    class VisitOrderViewHolder(private val binding: ItemVisitOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userMeasurement: UserMeasurement) {
            binding.textGridPoint.text = "Grid Point ID: ${userMeasurement.closestGridPointId}"
            binding.textEuclideanDistance.text = "Euclidean Distance: ${userMeasurement.euclideanDistance ?: "N/A"}"
        }
    }
}
