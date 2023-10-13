package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread



class AddObservation : AppCompatActivity() {

    private lateinit var geocoder: Geocoder
    private lateinit var userLocation : Location
    private lateinit var etSelectSpecies:EditText
    private lateinit var etWhen: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observation)

        // Send user back
        val cancelTextView: TextView = findViewById(R.id.btnCancel)
        cancelTextView.setOnClickListener {
            onBackPressed()
        }

        requestLocation()

        etSelectSpecies = findViewById(R.id.etSelectSpecies)
        etWhen = findViewById(R.id.etWhen)

        etWhen.setOnClickListener{
            showCalendarDialog()
        }

        geocoder = Geocoder(this, Locale.getDefault())

//        thread {
//            val bird = try {
//               // val countryCode = getCountryCodeFromLocation(userLocation)
//                val apiWorker = APIWorker()
//                apiWorker.querySpeciesPerRegion("ZA-WC")?.readText()
//            } catch (e: Exception) {
//                null
//            }
//
//            // Print the result to the console
//            Log.d("Bird species data:", "$bird")
//            val speciesList = extractSpeciesListFromJson(bird)
//            etSelectSpecies.setOnClickListener {
//                showSpeciesDialog(speciesList)
//            }
//            if (bird != null) {
//                //extractFromJSON(bird)
//            }
//        }
    }

        // Extracts species from returned JSON
        private fun extractSpeciesListFromJson(jsonResponse: String?): List<String> {
            val speciesList = mutableListOf<String>()

            jsonResponse?.let {
                try {
                    val jsonArray = JSONArray(it)

                    // Extract species names from the JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val speciesName = jsonArray.getString(i)
                        speciesList.add(speciesName)
                    }
                } catch (e: JSONException) {
                    Log.e("JSON Parsing Error", "Error parsing JSON", e)
                }
            }

            return speciesList
        }

    //  Request users' current location
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
                    Log.d("Region Code",getCountryCodeFromLocation(userLocation))
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
    private fun showSpeciesDialog(speciesList: List<String>) {
        val dialogView = layoutInflater.inflate(R.layout.species_dialog, null)
        val etSearch = dialogView.findViewById<EditText>(R.id.etSearch)
        val listView = dialogView.findViewById<ListView>(android.R.id.list)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setTitle("Select a species")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()

        // Create an adapter for the list view using the provided species list
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, speciesList)
        listView.adapter = adapter

        // Handle search functionality
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Handle item click
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedSpecies = adapter.getItem(position)
            etSelectSpecies.setText(selectedSpecies)
            dialog.dismiss()
        }

        dialog.show()
    }

    //  Calender Dialog
    private fun showCalendarDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                etWhen.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

}