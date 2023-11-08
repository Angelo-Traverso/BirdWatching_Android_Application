package com.example.opsc7312_poe_birdwatching

import com.example.opsc7312_poe_birdwatching.Models.BirdModel
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class ChallengeModel {
    companion object {
        //challenges vars
        var newTopRoundInDuckHunt = 0
        var newTripsCompleted = 0

        var topRoundInDuckHunt = 0
        var tripsCompleted = 0

        fun getChallenges() {
            val db = FirebaseFirestore.getInstance()

            val currentUserUID =
                ToolBox.users[0].UserID

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = dateFormat.format(Date())

            val usersCollection = db.collection("users")

            val userProgressCollection =
                usersCollection.document(currentUserUID).collection("progress")

            val query = userProgressCollection.whereEqualTo("date", currentDate)

            // Execute the query
            query.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val challengeData = document.data
                        val newTopScore = challengeData["newTopScore"] as Number
                        topRoundInDuckHunt = newTopScore.toInt()
                        val newTripsCompleted = challengeData["newTripsCompleted"] as Number
                        tripsCompleted = newTripsCompleted.toInt()
                    }
                }
                .addOnFailureListener { e ->
                    var a = e.message
                }
        }

        fun saveChallenge() {

            if (newTripsCompleted > tripsCompleted || newTopRoundInDuckHunt > topRoundInDuckHunt) {

                if (newTripsCompleted > tripsCompleted) {
                    tripsCompleted = newTripsCompleted
                }

                if (newTopRoundInDuckHunt > topRoundInDuckHunt) {
                    topRoundInDuckHunt = newTopRoundInDuckHunt
                }

                val userId = ToolBox.users[0].UserID

                // Initialize Firebase Firestore
                val db = FirebaseFirestore.getInstance()

                // Get the current date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = dateFormat.format(Date())

                // Reference to the "users" collection
                val usersCollection = db.collection("users")

                // Reference to the specific user's "progress" collection for the current date
                val userProgressCollection =
                    usersCollection.document(userId).collection("progress")
                        .document(currentDate)

                // Create a data object for the user's progress for the current day
                val progressData = hashMapOf(
                    "date" to currentDate,
                    "newTopScore" to topRoundInDuckHunt,
                    "newTripsCompleted" to tripsCompleted,
                )

                // Add the progress data to the user's "progress" collection for the current day
                userProgressCollection.set(progressData)
                    .addOnSuccessListener { documentReference ->
                        // Progress data has been successfully added
                    }
                    .addOnFailureListener { e ->
                        var a = e.message
                    }

            }
        }

        fun updatePoints(points: Int){

        }
    }
}