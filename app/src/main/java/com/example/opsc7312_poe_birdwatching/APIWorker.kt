package com.example.opsc7312_poe_birdwatching

import android.net.Uri
import android.util.Log
import java.net.MalformedURLException
import java.net.URL

class APIWorker {

    //query e bird
    fun QueryeBird(lng: Double, lat: Double, dist: Double): URL? {
        val EBIRD_URL = "https://api.ebird.org/v2/ref/hotspot/geo?lat=${lat}&lng=${lng}&dist=${dist}"
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
        Log.i("URLCREATED", "URL: $url")
        return url
    }
}