package com.example.opsc7312_poe_birdwatching.Models

data class UsersModel(val UserID: String, val Fname: String, val Sname: String, val Email: String, val Hash: String, val Salt: String, val MeasurementUnits: Int, val MaxDistance: Int, val ChallengePoints: Int)
