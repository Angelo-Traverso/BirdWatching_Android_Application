package com.example.opsc7312_poe_birdwatching

import android.app.Application
import android.location.Location
import com.example.opsc7312_poe_birdwatching.Models.*

class ToolBox : Application() {

    companion object {
        //var user = UsersModel()
        var userRegion = ""
        var userID = -1
        var users = arrayListOf<UsersModel>()
        var usersObservations = arrayListOf<UserObservation>()
        var userLocation: Location? = null
        var hotspotSightings: List<SightingModel> = mutableListOf()
        var birds: List<BirdModel> = mutableListOf()
        var topRoundInDuckHunt = 0
        var tripsCompleted = 0
        var populated = false
    }
}