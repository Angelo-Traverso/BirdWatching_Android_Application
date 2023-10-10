package com.example.opsc7312_poe_birdwatching.Models

import java.sql.Time

data class User_Progress(val UserProgressID: String, val UserID: String, val ChallengeID: String, val TimeStamp: Time, val User_Progress: Int)
