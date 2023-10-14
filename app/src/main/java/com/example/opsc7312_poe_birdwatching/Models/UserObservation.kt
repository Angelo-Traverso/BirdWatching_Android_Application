package com.example.opsc7312_poe_birdwatching.Models

import android.location.Geocoder
import android.location.Location
import java.sql.Date
import java.sql.Time

data class UserObservation(val ObservationID: String, val UserID: String, val Date: Date, val BirdName: String, val Amount: String, val Location: Location)
