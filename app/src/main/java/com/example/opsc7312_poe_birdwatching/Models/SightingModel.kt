package com.example.opsc7312_poe_birdwatching.Models

// Used to store data retrieved from eBird
class SightingModel(
    val commonName: String,
    val howMany: Int,
    val date: String,
    val lat: Double,
    val lng: Double
)
