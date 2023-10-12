package com.example.opsc7312_poe_birdwatching.Models

data class UsersModel(var UserID: Int = -1, var Name: String = "", var Email: String = "", var Hash: String = "", var MeasurementUnitsIsKM: Boolean = true, var MaxDistance: Double = 5.0, var ChallengePoints: Int = 0)
