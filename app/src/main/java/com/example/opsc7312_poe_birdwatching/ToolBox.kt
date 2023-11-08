//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.app.Application
import android.icu.util.LocaleData
import android.location.Location
import com.example.opsc7312_poe_birdwatching.Models.BirdModel
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.example.opsc7312_poe_birdwatching.Models.UserObservation
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDate

class ToolBox : Application() {

    companion object {
        var userRegion = ""

        //will only store one user at a time
        var users = arrayListOf<UsersModel>()

        //stores all entries of users observations
        var usersObservations = arrayListOf<UserObservation>()

        //used to store the sightings for a specific observation, changed for every hotpost pressed
        var hotspotsSightings: List<SightingModel> = mutableListOf()

        //used to store the birds found in a region, session based
        var birdsInTheRegion: List<BirdModel> = mutableListOf()

        //var for observers
        var populated = false
    }
}