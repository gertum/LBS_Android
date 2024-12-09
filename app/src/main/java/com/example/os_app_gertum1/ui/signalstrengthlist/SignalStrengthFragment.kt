package com.example.os_app_gertum1.ui.signalstrengthlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.ui.editsignalstrength.EditSignalStrengthActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignalStrengthListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signal_strength_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_signal_strength)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Example data
        val dummyData = listOf(
            SignalStrength(1, 1, "WiFi_1", -50), // Example data
            SignalStrength(2, 2, "WiFi_2", -60)
        )

        val adapter = SignalStrengthAdapter(dummyData)
        recyclerView.adapter = adapter

        // Handle the FloatingActionButton click event
        val fabAddSignal = view.findViewById<FloatingActionButton>(R.id.fab_add_signal)
        fabAddSignal.setOnClickListener {
            // Navigate to Add or Edit Signal activity
            val intent = Intent(requireContext(), EditSignalStrengthActivity::class.java)
            startActivity(intent)
        }
    }
}

