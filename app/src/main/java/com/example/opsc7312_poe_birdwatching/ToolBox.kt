//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.app.Application
import com.example.opsc7312_poe_birdwatching.Models.BirdModel
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.UsersModel

class ToolBox : Application() {

    companion object {
        var userRegion = ""
        var userID = -1
        var users = arrayListOf<UsersModel>()
        var usersObservations = arrayListOf<UserObservation>()
        var hotspotSightings: List<SightingModel> = mutableListOf()
        var birds: List<BirdModel> = mutableListOf()
        var topRoundInDuckHunt = 0
        var tripsCompleted = 0
        var populated = false
    }
}