package com.example.opsc7312_poe_birdwatching

import android.net.Uri
import android.util.Log
import com.example.opsc7312_poe_birdwatching.Models.*
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import org.json.JSONArray
import java.net.MalformedURLException
import java.net.URL
import kotlin.concurrent.thread

class APIWorker {

    public fun getHotspots(lat: Double, lon: Double): List<HotspotModel> {
        var HotspotList: List<HotspotModel> = mutableListOf()

        val bird = try {
            queryGetHotspots(lon, lat, ToolBox.users[ToolBox.userID].MaxDistance)?.readText()
        } catch (e: Exception) {
            return HotspotList
        }

        if (!bird.isNullOrEmpty()) {
            HotspotList = extractHotSpots(bird)
            return HotspotList
        }

        return HotspotList
    }

    private fun extractHotSpots(birdJSON: String?): List<HotspotModel> {
        var HotspotList: MutableList<HotspotModel> = mutableListOf()

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
                    var newHotspot =
                        HotspotModel(location.name, location.latitude, location.longitude)
                    HotspotList.add(newHotspot)
                    //    println(newHotspot)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
        }

        return (HotspotList);
    }

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
        }
        // println("------------------ queryGetHotspots URL: $url")
        return url
    }

    public fun getBirds(): List<BirdModel> {
        var BirdList: List<BirdModel> = mutableListOf()

        //get species in region
        val birdSciNames = try {
            queryGetRegionalSciName(ToolBox.userRegion)?.readText() //takes region in, returns long json string
        } catch (e: Exception) {
            return BirdList
        }

        if (!birdSciNames.isNullOrEmpty()) {
            val birdNames = try {
                queryGetFullBirdData(extractRegionalSciName(birdSciNames))?.readText()
            } catch (e: Exception) {
                return BirdList
            }

            if (!birdNames.isNullOrEmpty()) {
                BirdList = extractBirdNames(birdNames)
                return BirdList
            }
        }
        return BirdList
    }

    private fun extractRegionalSciName(json: String): String {
        val jsonArray = JsonParser.parseString(json) as JsonArray
        val names = jsonArray.map { it.asString }
        return names.joinToString(",")
    }

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
            e.printStackTrace()
        }
        // println("------------------ queryGetSpecies URL: $url")
        return url
    }

    private fun extractBirdNames(birdJSON: String?): List<BirdModel> {
        val BirdList: MutableList<BirdModel> = mutableListOf()

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
                    BirdList.add(newBird)
                    // println(newBird.commonName)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return BirdList
    }

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
            e.printStackTrace()
        }
        //    println("------------------ queryGetSpeciesData URL: $url")
        return url
    }

    public  fun getHotspotBirdData(lat: Double, lon: Double): List<SightingModel>
    {
        var SightingsList: List<SightingModel> = mutableListOf()

        val sighting = try {
            queryGetHotspotBirdData(lon, lat)?.readText()
        } catch (e: Exception) {
            return SightingsList
        }

        if (!sighting.isNullOrEmpty()) {
            SightingsList = extractHotspotBirdData(sighting)
            Log.d("List Size !!!!!!",SightingsList.size.toString())
            return SightingsList
        }

        return SightingsList
    }

    private fun extractHotspotBirdData(birdJSON: String?): List<SightingModel>
    {
        val SightingsList: MutableList<SightingModel> = mutableListOf()

        if (!birdJSON.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(birdJSON)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val commonName = jsonObject.getString("comName")
                    val howMany = jsonObject.getInt("howMany")
                    val obsDate = jsonObject.getString("obsDt")

                    val newSighting = SightingModel(commonName, howMany, obsDate)
                    Log.d("MODEL!!!!!!!!!!!",newSighting.howMany.toString())
                    SightingsList.add(newSighting)
                    println(newSighting)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("MODEL!!!!!!!!!!!","Broken!!!!!!!!")
            }
        }

        Log.d("LOOPPPY","Didnt make LOOP")

        return SightingsList
    }

    private fun queryGetHotspotBirdData(lng: Double, lat: Double): URL?
    {
        val EBIRD_URL = "https://api.ebird.org/v2/data/obs/geo/recent?lat=${lat}&lng=${lng}&sort=species&back=30&dist=1&key=ijiunrr4nqen"
        val API_KEY = "ijiunrr4nqen"

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
           println("------------------ queryGetHotspotBirdData URL: $url")
        return url
    }
}