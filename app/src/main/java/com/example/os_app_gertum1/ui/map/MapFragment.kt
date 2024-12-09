package com.example.os_app_gertum1.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var db: AppDatabase // Your Room Database instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize OSMDroid configuration
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        Configuration.getInstance().load(requireContext(), requireActivity().getSharedPreferences("osmdroid", 0))

        // Setup the MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Zoom and center map
        mapView.controller.setZoom(15)
        mapView.controller.setCenter(GeoPoint(51.5074, -0.1278)) // London, for example

        // Enable current location overlay
        val locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)

        // Add a marker
        val marker = Marker(mapView)
        marker.position = GeoPoint(51.5074, -0.1278)
        marker.title = "London"
        mapView.overlays.add(marker)

        // Initialize your Room database instance
        db = AppDatabase.getDatabase(requireContext())

        // Insert dummy data into the database if needed
        insertDummyDataIfNeeded()

        // Draw the route on the map
        fetchAndDrawRoute()
    }

    private fun insertDummyDataIfNeeded() {
        val dao = db.measurementDao()

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
    }

    private fun fetchAndDrawRoute() {
        val measurements = db.measurementDao().getAllMeasurements() // Assuming LiveData

        measurements.observe(viewLifecycleOwner, Observer { measurements ->
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
        mapView.onResume() // Good practice for OSMDroid
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Good practice for OSMDroid
    }
}
