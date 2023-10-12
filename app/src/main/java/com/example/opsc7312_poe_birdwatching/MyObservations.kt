package com.example.opsc7312_poe_birdwatching

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import java.text.SimpleDateFormat
import java.util.*


class MyObservations : Fragment() {

    private lateinit var llObservationContainer: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Populate the UserObservation List in ToolBox
        populateUserObservation()
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_observations, container, false)

        llObservationContainer = view.findViewById(R.id.myObservationContainer)

        // Add a view for every element
        for (observation in ToolBox.usersObservations) {
            addObservationViewToContainer(observation)
        }
        return view
    }

    //  Populates the usersObservation List in ToolBox
    private fun populateUserObservation() {
        Log.d("What is going on", "IDK");
        val dateString = "2001/01/25"
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val utilDate: Date? = dateFormat.parse(dateString)

        val sqlDate = if (utilDate != null) {
            java.sql.Date(utilDate.time)
        } else {
            null
        }

        // Check if date is null before creating UserObservation
        if (sqlDate != null) {
            val userObservationEntry1 =
                UserObservation("1", "ST10081927", sqlDate, "Paco", ToolBox.userLocation ?: Location(""))
            val userObservationEntry2 = UserObservation(
                "2",
                "ST10081928",
                sqlDate,
                "What",
                ToolBox.userLocation ?: Location("")
            )
            val userObservationEntry3 = UserObservation(
                "3",
                "ST10081929",
                sqlDate,
                "Test",
                ToolBox.userLocation ?: Location("")
            )
            ToolBox.usersObservations.add(userObservationEntry1)
            ToolBox.usersObservations.add(userObservationEntry2)
            ToolBox.usersObservations.add(userObservationEntry3)
            ToolBox.usersObservations.forEach { Log.d("User Observation", it.toString()) }

        } else {
            // Error parsing to Date format
        }

    }

    //  Function Adds new view to container for every userObservation instance
    private fun addObservationViewToContainer(userObservation: UserObservation) {
        val inflater = LayoutInflater.from(requireContext())
        val observationView = inflater.inflate(R.layout.my_observations_display_layout, null)
        val line = inflater.inflate(R.layout.line, null)

        // Populate the fields with data from UserObservation
        observationView.findViewById<TextView>(R.id.tvBirdName).text = userObservation.BirdName
        observationView.findViewById<TextView>(R.id.tvScientificName).text =
            userObservation.BirdName
        // Displaying location - should display location normal name
        observationView.findViewById<TextView>(R.id.tvLocation).text =
            userObservation.Location.longitude.toString() + " " + userObservation.Location.latitude.toString()
        observationView.findViewById<TextView>(R.id.tvDateSpotted).text =
            userObservation.Date.toString()

        // Add the inflated custom view to the linear layout
        llObservationContainer.addView(observationView)
        llObservationContainer.addView(line)
    }

}