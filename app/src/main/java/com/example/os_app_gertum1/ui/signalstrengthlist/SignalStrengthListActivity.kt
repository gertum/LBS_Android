package com.example.os_app_gertum1.ui.signalstrengthlist


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.ui.signalstrengthlist.SignalStrengthAdapter

class SignalStrengthListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signal_strength_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_signal_strength)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Example data
        val dummyData = listOf(
            SignalStrength(id = 1, measurement = 1, sensor= "SensorA", strength = -50),
            SignalStrength(id = 2, measurement = 1, sensor = "SensorB", strength = -60)
        )


        val adapter = SignalStrengthAdapter(dummyData)
        recyclerView.adapter = adapter
    }
}