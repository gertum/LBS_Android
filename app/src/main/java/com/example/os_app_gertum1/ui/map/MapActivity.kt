package com.example.os_app_gertum1.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import com.example.os_app_gertum1.R

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize OSMDroid configuration
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)

        // Set up the map
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)  // Default OSM tiles

        // Zoom and center map
        mapView.controller.setZoom(15)
        mapView.controller.setCenter(GeoPoint(51.5074, -0.1278)) // London, for example

        // Add a marker
        val marker = Marker(mapView)
        marker.position = GeoPoint(51.5074, -0.1278)
        marker.title = "London"
        mapView.overlays.add(marker)
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
