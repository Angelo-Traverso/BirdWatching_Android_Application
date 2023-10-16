package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
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
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class Hotpots : AppCompatActivity(), OnMapReadyCallback {

    //map and location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var lat = 0.0
    private var lon = 0.0
    private var HotspotList = mutableListOf<HotspotModel>()
    private val sightingsList: List<SightingModel> = mutableListOf()

    //nav buttons
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var menuGame: FloatingActionButton
    private lateinit var menuSettings: FloatingActionButton
    private lateinit var menuAddObservation: FloatingActionButton
    private lateinit var menuMyObs: FloatingActionButton
    private lateinit var menuChallenges: FloatingActionButton

    //menu movement
    private lateinit var fabClose: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotpots)

        //MAP code
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //NAV menu popup code
        //region
        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
        fabAnticlock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)

        fabMenu = findViewById(R.id.fabMenu)
        menuGame = findViewById(R.id.menu_game)
        menuSettings = findViewById(R.id.menu_settings)
        menuAddObservation = findViewById(R.id.menu_addObservation)
        menuMyObs = findViewById(R.id.menu_viewObservation)
        menuChallenges = findViewById(R.id.menu_challenges)

        menuMyObs.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            close()
        }
        menuGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            close()
        }
        menuAddObservation.setOnClickListener {
            val intent = Intent(this, AddObservation::class.java)
            startActivity(intent)
            close()
        }
        menuSettings.setOnClickListener {
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

    //when google maps is ready this code will execute
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //get the users current location
        getCurrentLocation { lat, lon ->
            this.lat = lat
            this.lon = lon

            //get the nearby hotspots
            getNearByHotspots()

            //add users Obs
            addUserObs()

            //move camera
            val userLocation = LatLng(lat, lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }

        // On Click for marker
        mMap.setOnMarkerClickListener { marker ->

            getLocationData(marker.position.latitude, marker.position.longitude)

            val intent = Intent(this, Navigation::class.java)
            intent.putExtra("LATITUDE", lat)
            intent.putExtra("LONGITUDE", lon)
            intent.putExtra("DEST_LAT", marker.position.latitude)
            intent.putExtra("DEST_LNG", marker.position.longitude)

            val bottomSheetFragment = BottomSheetHotspot()
            //bottomSheetFragment.displaySightingsInBottomSheet(this@Hotpots, ToolBox.hotspotSightings)
            bottomSheetFragment.show(supportFragmentManager, BottomSheetHotspot.TAG)


            // Navigation button click
            bottomSheetFragment.setButtonClickListener {
                startActivity(intent)
            }

            /*navigateToMarker(marker.position)*/
            true
        }
    }

    //markers
    //region

    //show any user obs on the map in a different color
    private fun addUserObs() {
        if (ToolBox.usersObservations.isNotEmpty()) {
            for (location in ToolBox.usersObservations) {
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            location.Location.latitude,
                            location.Location.longitude
                        )
                    )
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("User Sighting: " + location.BirdName)
                )
            }
        }
    }

    //get all nearby hostpsot to the user, based on their chosen distance
    private fun getNearByHotspots() {
        //query eBird and get the nearby hotspots and the birds in the region
        var apiWorker = APIWorker()
        val scope = CoroutineScope(Dispatchers.Default)

        thread {
            scope.launch {
                val hotspots = apiWorker.getHotspots(lat, lon)
                ToolBox.birds = apiWorker.getBirds()
                UpdateMarkers(hotspots)
            }
        }
    }

    //puts markers on map
    private fun UpdateMarkers(locations: List<HotspotModel>) {
        try {
            runOnUiThread {
                for (location in locations) {
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(location.Lat, location.Lon))
                            .title(location.Name)
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

    //method to handel the fusedLocationClient logic and if no lcoation is found use a hard coded location
    //this is a callback as it needs to finish what it is working on before the rest of the map logic can continue
    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = location.latitude
                    val lon = location.longitude
                    callback(lat, lon)
                } ?: callback(-33.9249, 18.4241)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    //method to query eBird and get data for a hotspot
    private fun getLocationData(lat: Double, lng: Double) {
        var apiWorker = APIWorker()
        val scope = CoroutineScope(Dispatchers.Default)

        //using threading to query external resorces
        thread {
            scope.launch {
                ToolBox.hotspotSightings = apiWorker.getHotspotBirdData(lat, lng)
            }
        }
    }

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
        menuSettings.startAnimation(fabOpen)
        menuAddObservation.startAnimation(fabOpen)
        menuMyObs.startAnimation(fabOpen)
        menuChallenges.startAnimation(fabOpen)
        isOpen = true

    }

    private fun isOpen(): Boolean {
        if (isOpen) {
            menuGame.startAnimation(fabClose)
            menuSettings.startAnimation(fabClose)
            menuAddObservation.startAnimation(fabClose)
            menuMyObs.startAnimation(fabClose)
            menuChallenges.startAnimation(fabClose)
            fabMenu.startAnimation(fabAnticlock)
            return true

        } else {
            fabMenu.startAnimation(fabClock)
            menuGame.startAnimation(fabOpen)
            menuSettings.startAnimation(fabOpen)
            menuAddObservation.startAnimation(fabOpen)
            menuMyObs.startAnimation(fabOpen)
            menuChallenges.startAnimation(fabOpen)
            return false
        }
    }

    private fun close() {
        menuGame.startAnimation(fabClose)
        menuSettings.startAnimation(fabClose)
        menuAddObservation.startAnimation(fabClose)
        menuMyObs.startAnimation(fabClose)
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