package com.example.opsc7312_poe_birdwatching.Models

import android.location.Location
import java.sql.Date

//data class used to store user observations/sightings
data class UserObservation(val ObservationID: String, val UserID: String, val Date: Date, val BirdName: String, val Amount: String, val Location: Location, val Note: String, val PlaceName: String)
