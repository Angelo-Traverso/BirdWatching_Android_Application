//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


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

    //==============================================================================================
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
            showSpeciesDialog(ToolBox.birdsInTheRegion)
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

        //start the handler
        if(ToolBox.populated)
        {
            btnSave.isEnabled = true
            etNote.isEnabled = true
            etHowMany.isEnabled = true
            etWhen.isEnabled = true
            etSelectSpecies.isEnabled = true
            println("data exists")
        }
        else
        {
            btnSave.isEnabled = false
            etNote.isEnabled = false
            etHowMany.isEnabled = false
            etWhen.isEnabled = false
            etSelectSpecies.isEnabled = false
            handler.post(checkPopulatedRunnable)
            println("handeler started")
        }

    }

    //==============================================================================================
    //Source: (ChatGPT, n.d.)
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

    //==============================================================================================
    //save the new obs to the list
    private fun addNewObs() {
        try {
            if (validateForm()) {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy") // Use the correct format
                val dateInput = etWhen.text.toString().trim()
                val birdName = etSelectSpecies.text.toString().trim()
                val howMany = etHowMany.text.toString().trim().toInt()
                val note = etNote.text.toString().trim()

                val date = dateFormat.parse(dateInput)

                var location = userLocation
                if (ToolBox.newObsOnHotspot)
                {
                    location = (Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = ToolBox.newObslat
                        longitude = ToolBox.newObslng
                    })
                }

                if (date != null) {
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
                    val observation = UserObservation(
                        "",
                        ToolBox.users[0].UserID,
                        formattedDate, // Use the formatted date
                        birdName,
                        howMany.toInt(),
                        location,
                        note,
                        "",
                        ToolBox.newObsOnHotspot
                    )

                    ToolBox.newObsOnHotspot = false
                    val db = FirebaseFirestore.getInstance()
                    val observationsCollection = db.collection("observations")

                    observationsCollection
                        .add(observation)
                        .addOnSuccessListener { documentReference ->
                            // Observation added to Firestore successfully
                            Toast.makeText(this, "Bird observation saved!", Toast.LENGTH_LONG).show()

                            ToolBox.usersObservations.add(observation)

                            // Clear all input fields
                            //clearFields(etSelectSpecies, etHowMany, etWhen, etNote)

                            val intent = Intent(this, Hotpots::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            // Handle the error if adding the observation to Firestore fails
                            Toast.makeText(this, "Failed to save bird observation: ${e.message}", Toast.LENGTH_SHORT).show()
                            print(e.message)
                        }
                }
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //==============================================================================================
    // Method to clear all input fields
    private fun clearFields(species:EditText, howMany: EditText, date: EditText, note: EditText)
    {
        species.text.clear()
        howMany.text.clear()
        date.text.clear()
        note.text.clear()

    }
    //==============================================================================================
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

            return valid
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }

    //==============================================================================================
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
            }
        }
    }

    //==============================================================================================
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

    //==============================================================================================
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

    //==============================================================================================
    //Source: (ChatGPT, n.d.)
    // Remove the runnable when the activity is destroyed to prevent memory leaks.
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkPopulatedRunnable)
    }
}