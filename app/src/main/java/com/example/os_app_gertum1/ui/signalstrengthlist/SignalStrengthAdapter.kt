package com.example.os_app_gertum1.ui.signalstrengthlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.R

class SignalStrengthAdapter(
    private var signals: List<SignalStrength>
) : RecyclerView.Adapter<SignalStrengthAdapter.SignalViewHolder>() {

    class SignalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.signal_name)
        val strength: TextView = view.findViewById(R.id.signal_strength)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_signal_strength, parent, false)
        return SignalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SignalViewHolder, position: Int) {
        val signal = signals[position]
        holder.name.text = signal.sensor
        holder.strength.text = "${signal.strength} dBm"
    }

    override fun getItemCount(): Int = signals.size

    // Method to update the data
    fun updateData(newSignals: List<SignalStrength>) {
        signals = newSignals
        notifyDataSetChanged() // Notify adapter that the data has changed
    }
}
