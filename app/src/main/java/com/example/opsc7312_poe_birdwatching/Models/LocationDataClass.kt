package com.example.opsc7312_poe_birdwatching.Models

data class LocationDataClass(
    val id: String,
    val countryCode: String,
    val regionCode: String,
    val unused1: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val date: String,
    val unused2: Int
)