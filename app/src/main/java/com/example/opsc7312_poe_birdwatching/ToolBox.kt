package com.example.opsc7312_poe_birdwatching

import android.app.Application
import android.location.Location
import com.example.opsc7312_poe_birdwatching.Models.*
import com.example.opsc7312_poe_birdwatching.Models.Challenges

class ToolBox : Application() {

    companion object {
        //var user = UsersModel()
        var userRegion = ""
        var userID = -1
        var users = arrayListOf<UsersModel>()
        var usersProgress = arrayListOf<User_Progress>()
        var usersObservations = arrayListOf<UserObservation>()
        var challenges = arrayListOf<Challenges>()
        var userLocation: Location? = null
        var hotspotSightings: List<SightingModel> = mutableListOf()
        var birds: List<BirdModel> = mutableListOf()
    }
}