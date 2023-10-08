package com.example.opsc7312_poe_birdwatching.Models

data class UsersModel(var UserID: String = "", var Fname: String = "", var Sname: String = "", var Email: String = "", var Hash: String = "", var Salt: String = "", var MeasurementUnitsIsKM: Boolean = true, var MaxDistance: Double = 5.0, var ChallengePoints: Int = 0)
