package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
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
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlin.concurrent.thread

class map : Fragment() {

    private val REQUEST_LOCATION_PERMISSION = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var mapView: MapView? = null
    private var HotspotList = mutableListOf<HotspotModel>()
    private var lat = 0.0
    private var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get user's location
            requestLocation()
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)

        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                    .pulseEnabled(true)
                    .pulseColor(Color.BLUE)
                    .pulseAlpha(.4f)
                    .elevation(5f)
                    .accuracyColor(Color.RED)
                    .pulseInterpolator(BounceInterpolator())
                    .build()

                val locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(requireContext(), it)
                    .locationComponentOptions(locationComponentOptions)
                    .build()

                mapboxMap.locationComponent.apply {
                    activateLocationComponent(locationComponentActivationOptions)
                    isLocationComponentEnabled = true
                    cameraMode = CameraMode.TRACKING_GPS
                    renderMode = RenderMode.COMPASS
                }

                // Check for location permission
                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    // Get user's location
                    fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location: Location? ->
                        location?.let {
                            lat = location.latitude
                            lon = location.longitude
                        }
                    }

                    //if no location found set it to the castle of good hope
                    if (lat == 0.0 && lon == 0.0) {
                        lat = -33.9249
                        lon = 18.4241
                    }

                    //move camera
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

                //create a new thread and query the api
                thread {
                    val bird = try {
                        var apiWorker = APIWorker()
                        apiWorker.QueryeBird(lon, lat, ToolBox.user.MaxDistance)?.readText()
                    } catch (e: Exception) {
                        return@thread
                    }

                    if (!bird.isNullOrEmpty())
                        extractFromJSON(bird)
                }
            }
        }
        return view
    }

    private fun requestLocation() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Move camera to the user's current location
                    val cameraPosition =
                        CameraPosition.Builder().target(LatLng(latitude, longitude))
                            .zoom(12.0)
                            .tilt(20.0)
                            .build()
                    mapView?.getMapAsync { mapboxMap ->
                        mapboxMap.cameraPosition = cameraPosition
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location again
                requestLocation()
            } else {
                // Permission denied, handle accordingly
                // You may display a message or disable location features
            }
        }
    }

    //extrract the data from the json resonse
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