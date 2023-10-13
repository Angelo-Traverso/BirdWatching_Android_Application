package com.example.opsc7312_poe_birdwatching

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHotspot : BottomSheetDialogFragment() {

    private var buttonClickListener: (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_hotspot, container, false)

        /*view.setBackgroundColor(Color.TRANSPARENT)*/
        // Find the button and set a click listener
        val startNavigationButton = view.findViewById<Button>(R.id.btnStartNavigation)
        startNavigationButton.setOnClickListener {
            // Notify the listener when the button is clicked
            buttonClickListener?.invoke()
        }
        return view

    }

    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }

    fun displaySightingsInBottomSheet(context: Context, sightings: List<SightingModel>) {
        val bottomSheetView = view?.findViewById<LinearLayout>(R.id.linearHotspotInformation)
        val inflater = LayoutInflater.from(context)

        for (sighting in sightings) {
            // Inflate the hotspot_sighting layout
            val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)

            // Set the sighting information in the included layout
            hotspotSightingView.findViewById<TextView>(R.id.tvCommonName).text =
                "Common Name: ${sighting.commonName}"
            hotspotSightingView.findViewById<TextView>(R.id.tvHowMany).text =
                "How Many: ${sighting.howMany}"
            hotspotSightingView.findViewById<TextView>(R.id.tvDate).text = "Date: ${sighting.date}"
            Log.d("Got iT!", sighting.howMany.toString())
            // Add the included layout to the bottom sheet
            bottomSheetView?.addView(hotspotSightingView)

            val line = View(requireContext())
            line.setBackgroundColor(Color.GRAY)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2
            )
            params.topMargin = 8
            line.layoutParams = params
            bottomSheetView?.addView(line)
        }
    }

}