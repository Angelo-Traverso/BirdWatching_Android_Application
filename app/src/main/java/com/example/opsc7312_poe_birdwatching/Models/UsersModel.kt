package com.example.opsc7312_poe_birdwatching.Models

// Data class used to store a user
data class UsersModel(
    var UserID: String = "",
    var Name: String = "",
    var Surname: String = "",
    var isUnitKM: Boolean = true,
    var MaxDistance: Double = 5.0,
    var ChallengePoints: Int = 0,
    var mapStyleIsDark: Boolean = false
)
