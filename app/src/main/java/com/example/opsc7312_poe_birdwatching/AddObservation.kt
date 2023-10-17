package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.opsc7312_poe_birdwatching.Models.BirdModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class AddObservation : AppCompatActivity(){

    private lateinit var geocoder: Geocoder
    private lateinit var userLocation: Location
    private lateinit var etSelectSpecies: EditText
    private lateinit var etWhen: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSave: Button
    private lateinit var etHowMany: EditText
    private lateinit var cancelTextView: TextView
    private lateinit var pbWaitToSignIn: ProgressBar
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observation)

        // Send user back
        cancelTextView = findViewById(R.id.btnCancel)
        cancelTextView.setOnClickListener {
            val intent = Intent(this, Hotpots::class.java)
            startActivity(intent)
        }

        requestLocation()
        geocoder = Geocoder(this, Locale.getDefault())

        etSelectSpecies = findViewById(R.id.etSelectSpecies)
        etSelectSpecies.setOnClickListener {
            showSpeciesDialog(ToolBox.birds)
        }

        etWhen = findViewById(R.id.etWhen)
        etWhen.setOnClickListener {
            showCalendarDialog()
        }

        btnSave = findViewById(R.id.btnSaveObservation)
        btnSave.setOnClickListener {
            addNewObs()
        }

        etHowMany = findViewById(R.id.etHowMany)
        etNote = findViewById(R.id.etNote)
        pbWaitToSignIn = findViewById(R.id.pbWaitForData)

        //start the handeler
        if(ToolBox.populated)
        {
            btnSave.isEnabled = true
            etNote.isEnabled = true
            etHowMany.isEnabled = true
            etWhen.isEnabled = true
            etSelectSpecies.isEnabled = true
        }
        else
        {
            btnSave.isEnabled = false
            etNote.isEnabled = false
            etHowMany.isEnabled = false
            etWhen.isEnabled = false
            etSelectSpecies.isEnabled = false
            handler.post(checkPopulatedRunnable)
        }

    }

    //Source: ChatGPT
    // Check for the populated value when the activity is created.
    private val checkPopulatedRunnable = object : Runnable {
        override fun run() {
            if (ToolBox.populated) {
                btnSave.isEnabled = true
                etNote.isEnabled = true
                etHowMany.isEnabled = true
                etWhen.isEnabled = true
                etSelectSpecies.isEnabled = true
                pbWaitToSignIn.visibility = View.GONE
            } else {
                btnSave.isEnabled = false
                etNote.isEnabled = false
                etHowMany.isEnabled = false
                etWhen.isEnabled = false
                etSelectSpecies.isEnabled = false
                pbWaitToSignIn.visibility = View.VISIBLE
                handler.postDelayed(this, 100)
            }
        }
    }

    //save the new obs to the list
    private fun addNewObs() {
        try {

            if (validateForm()) {
                val obsID = ""
                val userID = ToolBox.userID.toString()

                val dateText = etWhen.text.toString()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                val utilDate = dateFormat.parse(dateText)
                val sqlDate = Date(utilDate.time)

                val birdName = etSelectSpecies.text.toString().trim()
                val location = userLocation
                val howMany = etHowMany.text.toString().trim()
                val note = etNote.text.toString().trim()


                var apiWorker = APIWorker()
                val scope = CoroutineScope(Dispatchers.Default)

                thread {
                    scope.launch {
                        val placeName = apiWorker.CoordsToLocation(
                            userLocation.latitude, userLocation.longitude
                        )
                        var newObs = UserObservation(
                            obsID, userID, sqlDate, birdName, howMany, location, note, placeName
                        )
                        ToolBox.usersObservations.add(newObs)
                    }
                }


            }

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //validate the form
    private fun validateForm(): Boolean {
        try {
            var valid = true
            val birdName: String = etSelectSpecies.text.toString().trim()
            val date: String = etWhen.text.toString().trim()
            val amount: String = etHowMany.text.toString().trim()

            if (userLocation == null) {
                val toast = Toast.makeText(this, "Location required", Toast.LENGTH_LONG)
                toast.show()
                valid = false
            }

            if (TextUtils.isEmpty(birdName)) {
                etSelectSpecies.error = "Name is required"
                valid = false
            }

            if (TextUtils.isEmpty(date)) {
                etWhen.error = "Date is required"
                valid = false
            }

            if (TextUtils.isEmpty(amount)) {
                etHowMany.error = "Amount is required"
                valid = false
            }

            //try conver to date, if fails it will be handled in the exception
            val dateText = etWhen.text.toString()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val utilDate = dateFormat.parse(dateText)
            val sqlDate = Date(utilDate.time)

            return valid
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }

    //  Request users' current location
    private fun requestLocation() {
        Log.d("Location", "requestLocation called")
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                userLocation = location
                Log.d("Region Code", getCountryCodeFromLocation(userLocation))
            }
        }
    }

    //  Gets current region code
    private fun getCountryCodeFromLocation(location: Location): String {
        val addresses: List<Address>? =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val countryCode = addresses[0].countryCode
            // Do something with the country code (e.g., display it)
            Log.d("CountryCode", "Country Code: $countryCode")

            return countryCode
        } else {
            return "Not found"
        }
    }

    //Shows searchable list of all regional bird species
    private fun showSpeciesDialog(speciesList: List<BirdModel>) {
        val dialogView = layoutInflater.inflate(R.layout.species_dialog, null)
        val etSearch = dialogView.findViewById<EditText>(R.id.etSearch)
        val listView = dialogView.findViewById<ListView>(android.R.id.list)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView).setTitle("Select a species")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()

        val comName: MutableList<String> = mutableListOf()

        for (bird in speciesList) {
            comName.add(bird.commonName)
        }

        // Create an adapter for the list view using the provided species list
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, comName)
        listView.adapter = adapter

        etSearch.requestFocus()

        // Handle search functionality
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("AfterTextChanged", "Text: $s")
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
            this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                etWhen.setText(formattedDate)
            }, year, month, day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 1000
        datePickerDialog.show()
    }

    //Source: ChatGPT
    // Remove the runnable when the activity is destroyed to prevent memory leaks.
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkPopulatedRunnable)
    }
}