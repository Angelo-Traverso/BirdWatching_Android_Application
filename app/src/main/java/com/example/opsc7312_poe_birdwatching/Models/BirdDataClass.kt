package com.example.opsc7312_poe_birdwatching.Models

//temp data class only used to extract the data from the json containing the full bird information
data class BirdDataClass(
    val scientificName: String,
    val commonName: String,
    val speciesCode: String,
    val category: String,
    val taxonOrder: Double,
    val comNameCodes: String,
    val sciNameCodes: String,
    val bandingCodes: String,
    val order: String,
    val familyComName: String,
    val familySciName: String,
    val reportAs: String,
    val extinct: String,
    val extinctYear: String,
    val familyCode: String
)