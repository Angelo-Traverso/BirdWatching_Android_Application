package com.example.opsc7312_poe_birdwatching.Models

import android.location.Location

// Data class used to store user observations/sightings
data class UserObservation(
    val ObservationID: String,
    val UserID: String,
    val Date: String,
    val BirdName: String,
    val Amount: Int,
    val Location: Location,
    val Note: String,
    val PlaceName: String,
    var IsAtHotspot: Boolean
)
