package com.example.os_app_gertum1.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import com.example.os_app_gertum1.R
import com.example.os_app_gertum1.data.database.AppDatabase
import com.example.os_app_gertum1.data.database.Measurement
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var db: AppDatabase // Your Room Database instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map) // Ensure this layout contains a MapView

        // Initialize OSMDroid configuration
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        // Setup the MapView
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Enable current location overlay
        val locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)

        // Initialize your Room database instance
        db = AppDatabase.getDatabase(this)

        // Insert dummy data into the database if needed
        insertDummyDataIfNeeded()

        // Draw the route on the map
        fetchAndDrawRoute()
    }

    private fun insertDummyDataIfNeeded() {
        val dao = db.measurementDao()

        // This should ideally be done in a background thread
        Thread {
            CoroutineScope(Dispatchers.IO).launch {
                val existingMeasurements = dao.getAllMeasurements().value

                // Check if the database is empty
                if (existingMeasurements.isNullOrEmpty()) {
                    dao.insertMeasurement(
                        Measurement(id = 1, x = (54.6872 * 1E6).toInt(), y = (25.2797 * 1E6).toInt(), distance = 0f) // Vilnius
                    )
                    dao.insertMeasurement(
                        Measurement(id = 2, x = (54.6890 * 1E6).toInt(), y = (25.2815 * 1E6).toInt(), distance = 100f) // Close point
                    )
                    dao.insertMeasurement(
                        Measurement(id = 3, x = (54.6900 * 1E6).toInt(), y = (25.2830 * 1E6).toInt(), distance = 200f) // Another point
                    )
                }

            }
        }.start()
    }

    private fun fetchAndDrawRoute() {
        val measurements = db.measurementDao().getAllMeasurements() // Assuming LiveData

        measurements.observe(this, Observer { measurements ->
            val routePoints = mutableListOf<GeoPoint>()

            for (measurement in measurements) {
                val geoPoint = GeoPoint(measurement.x / 1E6, measurement.y / 1E6)
                routePoints.add(geoPoint)
            }

            val polyline = Polyline()
            polyline.setPoints(routePoints)
            mapView.overlays.add(polyline)

            if (routePoints.isNotEmpty()) {
                mapView.controller.setCenter(routePoints[0]) // Center map on the first point
                mapView.controller.setZoom(15.0) // Adjust zoom level
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
