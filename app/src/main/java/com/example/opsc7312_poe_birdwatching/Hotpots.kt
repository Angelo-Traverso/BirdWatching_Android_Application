package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.opsc7312_poe_birdwatching.Game.GameActivity
import com.example.opsc7312_poe_birdwatching.Models.HotspotModel
import com.example.opsc7312_poe_birdwatching.Models.LocationDataClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class Hotpots : AppCompatActivity(), OnMapReadyCallback {

    //map and location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var HotspotList = mutableListOf<HotspotModel>()
    private var lat = 0.0
    private var lon = 0.0

    //nav
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var menuGame: FloatingActionButton
    private lateinit var settings: FloatingActionButton
    private lateinit var addObservation: FloatingActionButton
    private lateinit var fab4: FloatingActionButton
    private lateinit var menuChallenges: FloatingActionButton

    //private lateinit var tvCurrentLocation: TextView
    private lateinit var fabClose: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotpots)

        //MAP
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //NAV
        //region
        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
        fabAnticlock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)

        fabMenu = findViewById(R.id.fabMenu)
        menuGame = findViewById(R.id.menu_game)
        settings = findViewById(R.id.menu_settings)
        addObservation = findViewById(R.id.menu_addObservation)
        fab4 = findViewById(R.id.menu_viewObservation)
        menuChallenges = findViewById(R.id.menu_challenges)


        menuGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            close()
        }
        addObservation.setOnClickListener {
            val intent = Intent(this, AddObservation::class.java)
            startActivity(intent)
            close()
        }
        settings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            close()

        }
        menuChallenges.setOnClickListener {
            loadChallengesFragment()
            close()
        }
        fabMenu.setOnClickListener {
            if (isOpen()) {
                close()
            } else {
                open()
            }
        }
        //endregion
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            // get users location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    lat = location.latitude
                    lon = location.longitude
                }
            }
            // On Click for marker
            mMap.setOnMarkerClickListener { marker ->
                navigateToMarker(marker.position)
                true
            }
            //if no location found set it to the castle of good hope
            if (lat == 0.0 && lon == 0.0) {
                lat = -33.9249
                lon = 18.4241
            }

            val userLocation = LatLng(lat, lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

            GetBirdData()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Open google maps with route enabled
    private fun navigateToMarker(markerPosition: LatLng) {

        // Check if the user's location is available
        if (lat != 0.0 && lon != 0.0) {

            // Creating a URI for Google Maps with the navigation intent
            val uri = "google.navigation:q=${markerPosition.latitude},${markerPosition.longitude}&mode=d".toUri()

            // Creating an intent to launch Google Maps for navigation
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")

            // Check if Google Maps app is available on the device
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)

            } else {
                // Google Maps app is not available, handle accordingly (e.g., open in a web browser)
                // You can modify this based on your app's requirements
                val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(browserIntent)
            }
        } else {

            // Location is not available
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()

        }
    }

    //hotspots
    //region

    //requests the bird data from the API using paramiters
    private fun GetBirdData() {
        //create a new thread and query the api
        thread {
            val bird = try {
                var apiWorker = APIWorker()
                apiWorker.QueryeBird(lon, lat, ToolBox.users[ToolBox.userID].MaxDistance)
                    ?.readText()
            } catch (e: Exception) {
                return@thread
            }

            if (!bird.isNullOrEmpty()) extractFromJSON(bird)
        }
    }

    //takes json responce and extracts data
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

                UpdateMarkers(locations);

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            var b = 0
        }
    }

    //puts markers on map
    private fun UpdateMarkers(locations: List<LocationDataClass>) {
        try {
            runOnUiThread {
                for (location in locations) {
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(location.latitude, location.longitude))
                            .title(location.name)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //endregion

    //location
    //region

    //requests location permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location on the map
                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                //permission denied
            }
        }
    }

    //endregion

    //nav popup
    //region

    private fun open() {
        fabMenu.startAnimation(fabClock)
        menuGame.startAnimation(fabOpen)
        settings.startAnimation(fabOpen)
        addObservation.startAnimation(fabOpen)
        fab4.startAnimation(fabOpen)
        menuChallenges.startAnimation(fabOpen)
        isOpen = true

    }

    private fun isOpen(): Boolean {
        if (isOpen) {
            menuGame.startAnimation(fabClose)
            settings.startAnimation(fabClose)
            addObservation.startAnimation(fabClose)
            fab4.startAnimation(fabClose)
            menuChallenges.startAnimation(fabClose)
            fabMenu.startAnimation(fabAnticlock)
            return true

        } else {
            fabMenu.startAnimation(fabClock)
            menuGame.startAnimation(fabOpen)
            settings.startAnimation(fabOpen)
            addObservation.startAnimation(fabOpen)
            fab4.startAnimation(fabOpen)
            menuChallenges.startAnimation(fabOpen)
            return false
        }
    }

    private fun close() {
        menuGame.startAnimation(fabClose)
        settings.startAnimation(fabClose)
        addObservation.startAnimation(fabClose)
        fab4.startAnimation(fabClose)
        menuChallenges.startAnimation(fabClose)
        fabMenu.startAnimation(fabAnticlock)
        isOpen = false
    }

    private fun loadChallengesFragment() {
        val challengesFragment = Challenges()
        supportFragmentManager.beginTransaction().replace(R.id.relMain, challengesFragment)
            .addToBackStack(null).commit()
    }
    //endregion

    //on...
    //region
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    //endregion
}