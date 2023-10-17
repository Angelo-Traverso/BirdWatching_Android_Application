package com.example.opsc7312_poe_birdwatching

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHotspot : BottomSheetDialogFragment() {

    private lateinit var totalSpeciesTextView: TextView
    /*
    * Button click listener for sheet button
    * */
    private var buttonClickListener: (() -> Unit)? = null

    /*
    * Behaviour initialization for sheet
    * */
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    /*
    * View Created
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_hotspot, container, false)

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



        // Testing execution order for view created vs displaySightingsInBottomSheet
        Log.d("View!!!!", "View Created")

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

        // Find the view in the top part of the bottom sheet
        val topViewToClick = view.findViewById<View>(R.id.dragLine)

        topViewToClick.setOnClickListener {
            // Expand the bottom sheet when the top view is clicked
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        // Sets peek height of sheet
        bottomSheetBehavior.peekHeight = 250


        totalSpeciesTextView = view.findViewById(R.id.tvNumOfSpecies)

        // Binding linearView
        val bottomSheetLayout = view.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)

        // Update heading text
        arguments?.getString(ARG_HEADING_TEXT)?.let {
            setBottomSheetHeadingText(it)
        }

        // Display the sightings in the bottom sheet
        displaySightingsInBottomSheet(bottomSheetLayout, ToolBox.hotspotSightings)


    }

    companion object {
        const val TAG = "BottomSheetHotspot"


        private const val ARG_HEADING_TEXT = "arg_heading_text"
        fun newInstance(headingText: String): BottomSheetHotspot {
            val fragment = BottomSheetHotspot()
            val args = Bundle()
            args.putString(ARG_HEADING_TEXT, headingText)
            fragment.arguments = args
            return fragment
        }
    }

    fun setBottomSheetHeadingText(newText: String) {
        val textView =view?.findViewById<TextView>(R.id.tvBottomSheetHeading)
        textView?.text = newText
    }
    /*
    * Button click listener for button on sheet
    * */
    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }

    /*
    * Dynamically displays hotspot data
    * */
    @SuppressLint("InflateParams")
    fun displaySightingsInBottomSheet(bottomSheetView: LinearLayout, sightings: List<SightingModel>) {
        // Testing execution order for view created vs displaySightingsInBottomSheet
        Log.d("Display!!!!", "Display called")
        val inflater = LayoutInflater.from(bottomSheetView.context)
        var counter = 0

        // Set number of species text
        totalSpeciesTextView.text = "${sightings.count()} species"


            for (sighting in sightings) {
            counter++
            val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)
            // Inflate the hotspot_sighting layout
            //val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)
            // Set margins
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 16, 0, 0)
            hotspotSightingView.layoutParams = layoutParams
            // TextViews
            val commonNameTextView = hotspotSightingView.findViewById<TextView>(R.id.tvCommonName)
            val howManyTextView = hotspotSightingView.findViewById<TextView>(R.id.tvHowMany)
            val dateTextView = hotspotSightingView.findViewById<TextView>(R.id.tvDate)

            // Set the sighting information in the included layout

            commonNameTextView.text = "Common Name: ${sighting.commonName}"
            howManyTextView.text = "How Many: ${sighting.howMany}"
            dateTextView.text = "Date: ${sighting.date}"

            // Add the included layout to the bottom sheet
            bottomSheetView.addView(hotspotSightingView)

            // Add a line separator between sightings
            val line = View(bottomSheetView.context)
            line.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            )
            line.alpha = 0.5f
            line.setBackgroundColor(Color.BLACK)
            bottomSheetView.addView(line)
        }
    }
}
