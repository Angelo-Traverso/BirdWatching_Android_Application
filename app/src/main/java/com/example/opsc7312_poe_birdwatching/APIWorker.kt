package com.example.opsc7312_poe_birdwatching

import android.net.Uri
import android.util.Log
import java.net.MalformedURLException
import java.net.URL

class APIWorker {

    //query e bird
    fun QueryeBird(lng: Double, lat: Double, dist: Double): URL? {
        val EBIRD_URL = "https://api.ebird.org/v2/ref/hotspot/geo?lat=${lat}&lng=${lng}&dist=${dist}"
        val API_KEY = ""

        val buildUri: Uri = Uri.parse(EBIRD_URL).buildUpon().appendQueryParameter(
            "key", API_KEY
        ).build()
        var url: URL? = null
        try {
            url = URL(buildUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        Log.i("URLCREATED", "URL: $url")
        return url
    }
}

//override fun onMapReady() {
//
//    // Check for location permission
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        == PackageManager.PERMISSION_GRANTED) {
//
//        // get users lcoation
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                location?.let {
//                    val userLocation = LatLng(it.latitude, it.longitude)
//
//                }
//
//            }
//    } else {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    }
//}

////create a new thread and query the api
//thread {
//    val bird = try {
//        apiWorker.QueryeBird()?.readText()
//    } catch (e: Exception) {
//        return@thread
//    }
//
//    runOnUiThread { ExtractFromJSON(bird) }
//}

////extrract the data from the json resonse
//private fun ExtractFromJSON(birdJSON: String?) {
//    val linDisplay = findViewById<LinearLayout>(R.id.linOutput)
//
//    if (birdJSON != null) {
//        try {
//            val bList = JSONArray(birdJSON)
//
//            //for every entry returned by the query
//            for (i in 0 until bList.length()) {
//                val birdObject = BirdObject()
//                val bird = bList.getJSONObject(i)
//
//                // name
//                val birdName = bird.getString("comName")
//                birdObject.CommonName = birdName
//                // sci name
//                val sciName = bird.getString("sciName")
//                birdObject.ScienceName = sciName
//                // count
//                val amt = bird.getString("howMany")
//                birdObject.Count = amt.toInt()
//                // lat
//                val lat = bird.getString("lat")
//                birdObject.Latitude = lat.toDouble()
//                // long
//                val long = bird.getString("lng")
//                birdObject.Longitude = long.toDouble()
//                // location name, need to get the location name of the coords using open street map
//                val thread = Thread {
//                    birdObject.LocationName =
//                        apiWorker.CoordsToLocation(long.toDouble(), lat.toDouble())
//                }
//                thread.start()
//                thread.join()
//
//                // auto gen a textview for each bird and add it to the lin view
//                val output = TextView(this)
//                output.text =
//                    "Name: ${birdObject.CommonName}" +
//                            "\nCount: ${birdObject.Count}" +
//                            "\nLat: ${birdObject.Latitude} Lng: ${birdObject.Longitude}" +
//                            "\nPlace Name: ${birdObject.LocationName}" +
//                            "\n"
//                output.textSize = 15F
//                linDisplay.addView(output)
//
//                // add bird to list
//                BirdList.add(birdObject)
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
//}