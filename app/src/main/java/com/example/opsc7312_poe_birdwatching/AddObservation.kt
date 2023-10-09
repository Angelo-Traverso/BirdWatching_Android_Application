package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.concurrent.thread

private lateinit var geocoder: Geocoder
private lateinit var userLocation : Location
private lateinit var etSelectSpecies:EditText
class AddObservation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observation)

        requestLocation()

        etSelectSpecies = findViewById(R.id.etSelectSpecies)
        etSelectSpecies.setOnClickListener {
            showSpeciesDialog()
        }

        geocoder = Geocoder(this, Locale.getDefault())

        thread {
            val bird = try {
               // val countryCode = getCountryCodeFromLocation(userLocation)
                val apiWorker = APIWorker()
                apiWorker.querySpeciesPerRegion("ZA-WC")?.readText()
            } catch (e: Exception) {
                null
            }

            // Print the result to the console
            Log.d("Bird species data:", "$bird")

            if (bird != null) {
                //xtractFromJSON(bird)
            }
        }
    }

    private fun requestLocation() {
        Log.d("Location", "requestLocation called")
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
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
                    userLocation = location

                    // Perform reverse geocoding to get country/region code
                   // getCountryCodeFromLocation(location)


                } else {

                }
            }
    }

    //  Gets current region code
    private fun getCountryCodeFromLocation(location: Location) : String{
        val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val countryCode = addresses[0].countryCode
            // Do something with the country code (e.g., display it)
            Log.d("CountryCode", "Country Code: $countryCode")

            return countryCode
        }else
        {
            return "Not found"

        }
    }
    private fun showSpeciesDialog() {
        val speciesList = arrayOf("")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a species")
        builder.setItems(speciesList) { _, which ->
            // Handle the selected species
            val selectedSpecies = speciesList[which]
            etSelectSpecies.setText(selectedSpecies)
        }

        val dialog = builder.create()
        dialog.show()
    }
}