//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

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

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_observations, container, false)

        llObservationContainer = view.findViewById(R.id.myObservationContainer)

        // Fetch user observations and populate the list
        fetchUserObservations()

        return view
    }

    //==============================================================================================
    //  Function fetches user observations from their profile
    private fun fetchUserObservations() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userObservationsCollection = db.collection("observations")

            ToolBox.usersObservations.clear()

            userObservationsCollection
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val data = document.data

                        val timestamp = data["date"] as? com.google.firebase.Timestamp
                        val date = timestamp?.toDate()?.time?.let { Date(it) } ?: Date(0)

                        val locationData = data["location"] as? Map<String, Any>
                        val latitude = locationData?.get("latitude") as? Double ?: 0.0
                        val longitude = locationData?.get("longitude") as? Double ?: 0.0

                        val location = Location("fused")
                        location.latitude = latitude
                        location.longitude = longitude

                        val observation = UserObservation(
                            ObservationID = data["observationID"] as? String ?: "",
                            UserID = data["userID"] as? String ?: "",
                            Date = data["date"] as String ?: "",
                            BirdName = data["birdName"] as? String ?: "",
                            Amount = data["amount"] as? String ?: "",
                            Location = location,
                            Note = data["note"] as? String ?: "",
                            PlaceName = data["placeName"] as? String ?: ""
                        )

                        ToolBox.usersObservations.add(observation)
                    }

                    populateObservationViews()
                }
                .addOnFailureListener { exception ->
                    Log.e("MyObservations", "Error fetching observations: $exception")
                }
        }
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

            // Other UI population code here
            llObservationContainer.addView(observationView)
            llObservationContainer.addView(line)
        }
    }
}