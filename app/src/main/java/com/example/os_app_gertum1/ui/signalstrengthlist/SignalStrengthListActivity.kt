package com.example.os_app_gertum1.ui.signalstrengthlist
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.ui.editsignalstrength.EditSignalStrengthActivity
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.ui.signalstrengthlist.SignalStrengthAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignalStrengthListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signal_strength_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_signal_strength)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Example data
        val dummyData = listOf(
            SignalStrength(1, 1, "WiFi_1", -50), // Example data
            SignalStrength(2, 2, "WiFi_2", -60)
        )

        val adapter = SignalStrengthAdapter(dummyData)
        recyclerView.adapter = adapter

        // Handle the FloatingActionButton click event
        val fabAddSignal = findViewById<FloatingActionButton>(R.id.fab_add_signal)
        fabAddSignal.setOnClickListener {
            // Navigate to Add or Edit Signal activity
            val intent = Intent(this, EditSignalStrengthActivity::class.java)
            startActivity(intent)
        }
    }
}
