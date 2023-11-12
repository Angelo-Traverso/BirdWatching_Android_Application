package com.example.opsc7312_poe_birdwatching.Models

// Dataclass used when creating and tracking challenges
data class ChallengeObject(val description: String, var progress: Int, val required: Int, val pointsToGet: Int, val pointsAwarded: Int)
