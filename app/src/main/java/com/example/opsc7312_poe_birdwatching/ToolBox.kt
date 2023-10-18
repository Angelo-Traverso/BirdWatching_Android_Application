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
import java.sql.Date

class ToolBox : Application() {

    companion object {
        var userRegion = ""
        var userID = -1
        var users = arrayListOf<UsersModel>()
        var usersObservations = arrayListOf<UserObservation>(
            UserObservation(
                "0",
                0,
                Date(2023 - 10 - 18),
                "Red-chested Cuckoo",
                "2",
                Location = Location("gps").apply {
                    latitude = -34.005997
                    longitude = 18.465918
                },
                "",
                ""
            ),
            UserObservation(
                "1",
                0,
                Date(2023 - 10 - 18),
                "Quailfinch",
                "10",
                Location = Location("gps").apply {
                    latitude = -34.016235
                    longitude = 18.457296
                },
                "There were many birds all eating fruit",
                ""
            ),
            UserObservation(
                "2",
                0,
                Date(2023 - 10 - 18),
                "Blue Petrel",
                "2",
                Location = Location("gps").apply {
                    latitude = -34.030784
                    longitude = 18.477414
                },
                "",
                ""
            ),
            UserObservation(
                "3",
                0,
                Date(2023 - 10 - 18),
                "Marsh Owl",
                "3",
                Location = Location("gps").apply {
                    latitude = -34.002017
                    longitude = 18.452186
                },
                "",
                ""
            ),
            UserObservation(
                "4",
                1,
                Date(2023 - 10 - 18),
                "Grey Wagtail",
                "1",
                Location = Location("gps").apply {
                    latitude = -34.033745
                    longitude = 18.458073
                },
                "",
                ""
            ),
            UserObservation(
                "5",
                1,
                Date(2023 - 10 - 18),
                "Emu",
                "1",
                Location = Location("gps").apply {
                    latitude = -33.813011
                    longitude = 18.371997
                },
                "",
                ""
            ),
            UserObservation(
                "6",
                2,
                Date(2023 - 10 - 18),
                "Speckled Pigeon",
                "1",
                Location = Location("gps").apply {
                    latitude = -33.971619
                    longitude = 18.411377
                },
                "",
                ""
            ),
            UserObservation(
                "7",
                2,
                Date(2023 - 10 - 18),
                "Rock Pigeon",
                "1",
                Location = Location("gps").apply {
                    latitude = -33.958743
                    longitude = 18.459167
                },
                "",
                ""
            )
        )
        var hotspotSightings: List<SightingModel> = mutableListOf()
        var birds: List<BirdModel> = mutableListOf()
        var topRoundInDuckHunt = 0
        var tripsCompleted = 0
        var populated = false
    }
}