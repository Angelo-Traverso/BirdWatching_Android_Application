//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.net.Uri
import android.util.Log
import com.example.opsc7312_poe_birdwatching.Models.*
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class APIWorker {

    //==============================================================================================
    // Get nearby hotspots
    fun getHotspots(lat: Double, lon: Double): List<HotspotModel> {
        var hotspotList: List<HotspotModel> = mutableListOf()

        val bird = try {
            queryGetHotspots(lon, lat, ToolBox.users[0].MaxDistance)?.readText()
        } catch (e: Exception) {
            println("========================================== getHotspots " + e.toString())
            return hotspotList
        }

        if (!bird.isNullOrEmpty()) {
            hotspotList = extractHotSpots(bird)
            return hotspotList
        }

        return hotspotList
    }

    //==============================================================================================
    // Query e-bird to get hotspots near a geo point
    private fun queryGetHotspots(lng: Double, lat: Double, dist: Double): URL? {
        val EBIRD_URL =
            "https://api.ebird.org/v2/ref/hotspot/geo?lat=${lat}&lng=${lng}&dist=${dist}"
        val API_KEY = "ijiunrr4nqen"

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            println("========================================== queryGetHotspots" + e.toString())
        }
        return url
    }

    //==============================================================================================
    //extract the hotspots from the json response
    private fun extractHotSpots(birdJSON: String?): List<HotspotModel> {
        val hotspotList: MutableList<HotspotModel> = mutableListOf()

        if (!birdJSON.isNullOrEmpty()) {
            try {

                birdJSON.trimIndent()

                val locations = birdJSON.lines().map { line ->
                    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    if (parts.size >= 9) {
                        LocationDataClass(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4].toDouble(),
                            parts[5].toDouble(),
                            parts[6],
                            parts[7],
                            parts[8].toInt()
                        )
                    } else {
                        null
                    }
                }.filterNotNull()

                // Add hotspots to the list
                for (location in locations) {
                    ToolBox.userRegion = location.regionCode
                    val newHotspot =
                        HotspotModel(location.name, location.latitude, location.longitude)
                    hotspotList.add(newHotspot)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                println("==========================================" + e.toString())
            }
        }

        return (hotspotList);
    }

    //==============================================================================================
    // Getting regional birds
    fun getBirds(): List<BirdModel> {
        var birdList: List<BirdModel> = mutableListOf()

        // Get species in region, returns scientific names
        val birdSciNames = try {
            // Takes region in, returns long json string
            queryGetRegionalSciName(ToolBox.userRegion)?.readText()
        } catch (e: Exception) {
            println("========================================== getBirds$e")
            return birdList
        }

        //==============================================================================================
        // Gets the full data of the birds in the region
        if (!birdSciNames.isNullOrEmpty()) {
            val birdNames = try {
                queryGetFullBirdData(extractRegionalSciName(birdSciNames))?.readText()
            } catch (e: Exception) {
                println("========================================== getBirds$e")
                return birdList
            }

            // Extract the common bird names
            if (!birdNames.isNullOrEmpty()) {
                birdList = extractBirdNames(birdNames)
                return birdList
            }
        }
        return birdList
    }

    //==============================================================================================
    // Converts the json containing the birds sci names into a string that can be inserted into a query
    private fun extractRegionalSciName(json: String): String {
        val jsonArray = JsonParser.parseString(json) as JsonArray
        val names = jsonArray.map { it.asString }
        return names.joinToString(",")
    }

    //==============================================================================================
    // Gets a json of all birds in a sub region (eg. ZA-WC)
    private fun queryGetRegionalSciName(region: String): URL? {
        val EBIRD_URL = "https://api.ebird.org/v2/product/spplist/${region}"
        val API_KEY = "ijiunrr4nqen"

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            println("========================================== queryGetRegionalSciName$e")
            e.printStackTrace()
        }
        // println("------------------ queryGetSpecies URL: $url")
        return url
    }

    //==============================================================================================
    // Extracts the common bird names from the long data response containing all bird data
    private fun extractBirdNames(birdJSON: String?): List<BirdModel> {
        val birdList: MutableList<BirdModel> = mutableListOf()

        if (!birdJSON.isNullOrEmpty()) {
            try {
                val lines = birdJSON.lines()
                val header = lines[0].split(",")

                // Skip the header line and parse the remaining lines into BirdData objects
                val birdDataList = lines.subList(1, lines.size).map { line ->
                    val values = line.split(",")
                    if (values.size == header.size) {
                        BirdDataClass(
                            values[0],
                            values[1],
                            values[2],
                            values[3],
                            values[4].toDouble(),
                            values[5],
                            values[6],
                            values[7],
                            values[8],
                            values[9],
                            values[10],
                            values[11],
                            values[12],
                            values[13],
                            values[14]
                        )
                    } else {
                        null
                    }
                }.filterNotNull()

                for (birdData in birdDataList) {
                    val newBird = BirdModel(
                        birdData.commonName
                    )
                    birdList.add(newBird)
                }
            } catch (e: Exception) {
                println("========================================== extractBirdNames$e")
                e.printStackTrace()
            }
        }

        return birdList
    }

    //==============================================================================================
    // Gets the full information of all the birds in the region
    private fun queryGetFullBirdData(names: String): URL? {
        val EBIRD_URL = "https://api.ebird.org/v2/ref/taxonomy/ebird?species=${names}"
        val API_KEY = "ijiunrr4nqen"

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            println("========================================== queryGetFullBirdData$e")
            e.printStackTrace()
        }
        //    println("------------------ queryGetSpeciesData URL: $url")
        return url
    }

    //==============================================================================================
    // Used to get the data for all sightings near the chosen hotspot
    public  fun getHotspotBirdData(lat: Double, lon: Double): List<SightingModel>
    {
        var sightingsList: List<SightingModel> = mutableListOf()

        val sighting = try {
            queryGetHotspotBirdData(lon, lat)?.readText()
        } catch (e: Exception) {
            println("========================================== getHotspotBirdData$e")
            return sightingsList
        }

        if (!sighting.isNullOrEmpty()) {
            sightingsList = extractHotspotBirdData(sighting)
            return sightingsList
        }

        return sightingsList
    }

    //==============================================================================================
    // Gets the data of all sightings near a hotspot
    private fun queryGetHotspotBirdData(lng: Double, lat: Double): URL?
    {
        val EBIRD_URL = "https://api.ebird.org/v2/data/obs/geo/recent?lat=${lat}&lng=${lng}&sort=species&back=30&dist=1"
        val API_KEY = "ijiunrr4nqen"

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            println("========================================== queryGetHotspotBirdData$e")
            e.printStackTrace()
        }
        println("------------------ queryGetHotspotBirdData URL: $url")
        return url
    }

    //==============================================================================================
    // Extract the signings data into a usable object list
    private fun extractHotspotBirdData(birdJSON: String?): List<SightingModel>
    {
        val sightingsList: MutableList<SightingModel> = mutableListOf()

        if (!birdJSON.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(birdJSON)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val commonName = jsonObject.getString("comName")
                    val howMany = jsonObject.getInt("howMany")
                    val obsDate = jsonObject.getString("obsDt")
                    val lat = jsonObject.getDouble("lat")
                    val lng = jsonObject.getDouble("lng")

                    val newSighting = SightingModel(commonName, howMany, obsDate, lat, lng)
                    sightingsList.add(newSighting)
                    println(newSighting)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("========================================== extractHotspotBirdData$e")
            }
        }

        return sightingsList
    }
}