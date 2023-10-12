package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.opsc7312_poe_birdwatching.Game.GameActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import java.util.*

class Hotpots : AppCompatActivity() {

    private val REQUEST_LOCATION_PERMISSION = 1001

    private var isMenuVisible = false;
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var menuGame: FloatingActionButton
    private lateinit var settings: FloatingActionButton
    private lateinit var addObservation: FloatingActionButton
    private lateinit var fab4: FloatingActionButton
    private lateinit var menuChallenges: FloatingActionButton
    private lateinit var tvCurrentLocation: TextView

    private lateinit var fabClose: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hotpots)

        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        tvCurrentLocation = findViewById(R.id.tvCurrentLocation)

        val fragment = map()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

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

        menuGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            close()
        }

        addObservation.setOnClickListener{
            val intent = Intent(this, AddObservation::class.java )
            startActivity(intent)
            close()

        }

        settings.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            close()

        }

        menuChallenges.setOnClickListener{
            loadChallengesFragment()
            close()
        }

        fabMenu.setOnClickListener {
            if(isOpen())
            {
                close()
            }else
            {
                open()
            }
        }

    }

    private fun requestLocation() {
        Log.d("Location", "requestLocation called")
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    tvCurrentLocation.text = "Latitude: $latitude, Longitude: $longitude"

                    // Perform reverse geocoding to get country/region code
                  //  getCountryCodeFromLocation(location)
                } else {
                    tvCurrentLocation.text = "Location not available"
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now access the user's location
                    // Call a function to start receiving location updates
                } else {
                    // Permission denied
                    // Handle accordingly (e.g., display a message or disable location features)
                }
            }
        }
    }

    private fun open()
    {
        fabMenu.startAnimation(fabClock)
        menuGame.startAnimation(fabOpen)
        settings.startAnimation(fabOpen)
        addObservation.startAnimation(fabOpen)
        fab4.startAnimation(fabOpen)
        menuChallenges.startAnimation(fabOpen)
        isOpen = true

    }

    private fun isOpen() : Boolean
    {
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

    private fun close()
    {
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.relMain, challengesFragment)
            .addToBackStack(null) // If you want to allow back navigation
            .commit()
    }
}