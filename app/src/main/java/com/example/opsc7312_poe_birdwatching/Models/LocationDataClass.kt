package com.example.opsc7312_poe_birdwatching.Models

//a temp dataclass only used to extract data from the response from ebird when getting hotspots near the user
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