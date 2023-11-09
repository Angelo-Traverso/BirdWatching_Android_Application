//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.content.Intent
import android.content.Intent.getIntent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Date

class MyObservations : Fragment() {
    private lateinit var llObservationContainer: LinearLayout
    var lat = 0.0
    var lng = 0.0


    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_observations, container, false)

        llObservationContainer = view.findViewById(R.id.myObservationContainer)

        this.lat = ToolBox.lat
        this.lng = ToolBox.lng

        // Fetch user observations and populate the list
        //fetchUserObservations()
        populateObservationViews()
        return view
    }

    //==============================================================================================
    //  Function Populates observation view dynamically
    private fun populateObservationViews() {
        llObservationContainer.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        for (userObservation in ToolBox.usersObservations) {
            val observationView = inflater.inflate(R.layout.my_observations_display_layout, null)
            val line = inflater.inflate(R.layout.line, null)

            observationView.findViewById<TextView>(R.id.tvBirdName).text =
                userObservation.BirdName + " (x${userObservation.Amount})"

            val latitude = userObservation.Location.latitude
            val longitude = userObservation.Location.longitude

            // Displaying location - should display location normal name
            observationView.findViewById<TextView>(R.id.tvLocation).text =
                userObservation.PlaceName + "\n" + "Latitude: $latitude, Longitude: $longitude"

            observationView.findViewById<TextView>(R.id.tvDateSpotted).text =
                userObservation.Date.toString()

            if (userObservation.Note.isNotEmpty()) {
                observationView.findViewById<TextView>(R.id.tvViewObsNote).text =
                    userObservation.Note
            } else {
                observationView.findViewById<TextView>(R.id.tvViewObsNote).isVisible = false
            }

            observationView.setOnClickListener(){
                val intent = Intent(requireContext(), Navigation::class.java)
                intent.putExtra("LATITUDE", this.lat)
                intent.putExtra("LONGITUDE", this.lng)
                intent.putExtra("DEST_LAT", userObservation.Location.latitude)
                intent.putExtra("DEST_LNG", userObservation.Location.longitude)

                startActivity(intent)
            }

            // Other UI population code here
            llObservationContainer.addView(observationView)
            llObservationContainer.addView(line)
        }
    }
}