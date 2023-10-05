package com.example.opsc7312_poe_birdwatching

import android.app.Application
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.User_Progress
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.example.opsc7312_poe_birdwatching.Models.Challenges

class ToolBox : Application() {

    companion object
    {
        var users = arrayListOf<UsersModel>()
        var usersProgress = arrayListOf<User_Progress>()
        var usersObservations = arrayListOf<UserObservation>()
        var challenges = arrayListOf<Challenges>()
    }
}