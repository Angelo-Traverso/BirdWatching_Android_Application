package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.HotspotModel
import com.example.opsc7312_poe_birdwatching.Models.LocationDataClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlin.concurrent.thread

class map : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    var mapView: MapView? = null
    var HotspotList = mutableListOf<HotspotModel>()
    var lat = 0.0
    var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        val tvCurrentLocation = view?.findViewById<TextView>(R.id.tvCurrentLocation)

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                //  Check for location permission
                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    //  Get users location
                    fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity())
                    { location: Location? ->
                        location?.let {
                            lat = location.latitude
                            lon = location.longitude


                        }
                    }

                    // If no location found set it to the castle of good hope
                    if (lat == 0.0 && lon == 0.0) {
                        lat = -33.9249
                        lon = 18.4241
                    }

                    // Move camera
                    val cameraPosition = CameraPosition.Builder().target(
                        LatLng(
                            lat, lon
                        )
                    ).zoom(12.0).tilt(20.0).build()
                    mapboxMap.cameraPosition = cameraPosition

                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }

                // Create a new thread and query the api
                thread {
                    val bird = try {
                        var apiWorker = APIWorker()
                        apiWorker.QueryeBird(lon, lat, ToolBox.user.MaxDistance)?.readText()
                    } catch (e: Exception) {
                        return@thread
                    }

                    extractFromJSON(bird)
                }
            }
        }

        return view
    }

    // Extract the data from the json response
    private fun extractFromJSON(birdJSON: String?) {
        if (!birdJSON.isNullOrEmpty()) {
            try {

                birdJSON.trimIndent()

                val locations = birdJSON.lines().map { line ->
                    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    if (parts.size >= 9) {
                        LocationDataClass(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4].toDouble(),
                            parts[5].toDouble(),
                            parts[6],
                            parts[7],
                            parts[8].toInt()
                        )
                    } else {
                        null
                    }
                }.filterNotNull()

                // Add hotspots to the list
                for (location in locations) {
                    var newHotspot =
                        HotspotModel(location.name, location.latitude, location.longitude)
                    HotspotList.add(newHotspot)
                    println(newHotspot)
                }

                // Add markers on the UI thread
                activity?.runOnUiThread {
                    mapView?.getMapAsync { mapboxMap ->
                        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                            for (location in locations) {
                                mapboxMap.addMarker(
                                    com.mapbox.mapboxsdk.annotations.MarkerOptions()
                                        .position(LatLng(location.latitude, location.longitude))
                                        .title(location.name)
                                )
                            }
                        }
                    }
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            var b = 0
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}