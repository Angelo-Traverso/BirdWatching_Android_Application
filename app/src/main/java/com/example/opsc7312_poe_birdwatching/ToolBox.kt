//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.app.Application
import android.icu.util.LocaleData
import android.location.Location
import android.util.Log
import com.example.opsc7312_poe_birdwatching.Models.BirdModel
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDate

class ToolBox : Application() {

    companion object {
        var userRegion = ""

        //will only store one user at a time
        var users = arrayListOf<UsersModel>()

        //stores all entries of users observations
        var usersObservations = arrayListOf<UserObservation>()

        //used to store the sightings for a specific observation, changed for every hotpost pressed
        var hotspotsSightings: List<SightingModel> = mutableListOf()

        //used to store the birds found in a region, session based
        var birdsInTheRegion: List<BirdModel> = mutableListOf()

        //var for observers
        var populated = false

        //var for hotpsot location when adding new obs to it
        var lat = 0.0
        var lng = 0.0
        var newObsOnHotspot = false;

        //==============================================================================================
        //  Function fetches user observations from their profile
        fun fetchUserObservations() {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val db = FirebaseFirestore.getInstance()
                val userObservationsCollection = db.collection("observations")

                usersObservations.clear()

                userObservationsCollection
                    .whereEqualTo("userID", userId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val data = document.data

                            val timestamp = data["date"] as? com.google.firebase.Timestamp
                            val date = timestamp?.toDate()?.time?.let { java.sql.Date(it) } ?: java.sql.Date(
                                0
                            )

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
                                PlaceName = data["placeName"] as? String ?: "",
                                IsAtHotspot = data["isAtHotspot"] as? Boolean ?: false,
                            )

                            usersObservations.add(observation)
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.e("MyObservations", "Error fetching observations: $exception")
                    }
            }
        }


    }


}