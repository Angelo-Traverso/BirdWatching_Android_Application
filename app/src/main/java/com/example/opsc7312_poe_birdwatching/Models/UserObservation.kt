package com.example.opsc7312_poe_birdwatching.Models

import android.location.Geocoder
import java.sql.Date
import java.sql.Time

data class UserObservation(val ObservationID: String, val UserID: String, val Date: Date, val BirdName: String, val Location: Geocoder)
