package com.example.opsc7312_poe_birdwatching

import android.annotation.SuppressLint
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHotspot : BottomSheetDialogFragment() {

    private var buttonClickListener: (() -> Unit)? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    /*
    * View Created
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("View!!!!", "View Created")
        //bottomSheetView = view.findViewById(R.id.linearHotspotInformation)

        // Obtain the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        // Set a callback to handle state changes
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle state changes if needed
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle slide if needed
            }
        })

        // Find the view in the top part of the bottom sheet you want to click to expand
        val topViewToClick = view.findViewById<View>(R.id.tvBottomSheetHeading)

        topViewToClick.setOnClickListener {
            // Expand the bottom sheet when the top view is clicked
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior.peekHeight = 600  // Change this value as needed

        Log.d("List Size!!!!",ToolBox.hotspotSightings.size.toString())
        val bottomSheetLayout = view.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)

        // Display the sightings in the bottom sheet
        displaySightingsInBottomSheet(bottomSheetLayout, ToolBox.hotspotSightings)

    }

    companion object {
        const val TAG = "BottomSheetHotspot"
    }

    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }

    @SuppressLint("InflateParams")
    fun displaySightingsInBottomSheet(bottomSheetView: LinearLayout, sightings: List<SightingModel>) {
        Log.d("Display!!!!", "Display called")
        val inflater = LayoutInflater.from(bottomSheetView.context)

        for (sighting in sightings) {
            // Inflate the hotspot_sighting layout
            val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)

            val commonNameTextView = hotspotSightingView.findViewById<TextView>(R.id.tvCommonName)
            val howManyTextView = hotspotSightingView.findViewById<TextView>(R.id.tvHowMany)
            val dateTextView = hotspotSightingView.findViewById<TextView>(R.id.tvDate)

            // Set the sighting information in the included layout
            commonNameTextView.text = "Common Name: ${sighting.commonName}"
            Log.d("Common name", sighting.commonName)
            howManyTextView.text = "How Many: ${sighting.howMany}"
            Log.d("How Many?", sighting.howMany.toString())
            dateTextView.text = "Date: ${sighting.date}"
            Log.d("Date", sighting.date)

            // Add the included layout to the bottom sheet
            bottomSheetView.addView(hotspotSightingView)

            // Add a line separator between sightings
            val line = View(bottomSheetView.context)
            line.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            )
            line.setBackgroundColor(Color.BLACK)
            bottomSheetView.addView(line)
        }
    }

}
