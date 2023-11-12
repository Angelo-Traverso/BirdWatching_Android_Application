//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.annotation.SuppressLint
import android.content.Intent
import kotlin.math.*
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.opsc7312_poe_birdwatching.Models.HotspotModel
import com.example.opsc7312_poe_birdwatching.Models.SightingModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.mapbox.bindgen.None

class BottomSheetHotspot : BottomSheetDialogFragment() {

    private lateinit var totalSpeciesTextView: TextView
    private lateinit var informationText: TextView

    /*
    * Button click listener for sheet button
    * */
    private var buttonClickListener: (() -> Unit)? = null

    /*
    * Behaviour initialization for sheet
    * */
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    //==============================================================================================
    companion object {
        const val TAG = "BottomSheetHotspot"

        private const val ARG_HEADING_TEXT = "arg_heading_text"
        private const val ARG_LAT = "arg_lat"
        private const val ARG_LON = "arg_lon"

        fun newInstance(headingText: String, lat: Double, lon: Double): BottomSheetHotspot {
            val fragment = BottomSheetHotspot()
            val args = Bundle()
            args.putString(ARG_HEADING_TEXT, headingText)
            args.putDouble(ARG_LAT, lat)
            args.putDouble(ARG_LON, lon)
            fragment.arguments = args
            return fragment
        }
    }

    //==============================================================================================
    // View created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_hotspot, container, false)

        // Start navigation small button
        val startNavigationSmallButton = view.findViewById<ImageButton>(R.id.btnNavigationSmall)
        startNavigationSmallButton.setOnClickListener {
            // Notify the listener when the button is clicked
            buttonClickListener?.invoke()
        }

        // Start navigation button
        val startNavigationButton = view.findViewById<Button>(R.id.btnStartNavigation)
        startNavigationButton.setOnClickListener {
            // Notify the listener when the button is clicked
            buttonClickListener?.invoke()
        }

        // Add signing button
        val addSightingButton = view.findViewById<Button>(R.id.btnAddObs)
        addSightingButton.setOnClickListener {
            ToolBox.newObsOnHotspot = true;
            val intent = Intent(requireContext(), AddObservation::class.java)
            startActivity(intent)
        }

        return view
    }

    //==============================================================================================
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
        bottomSheetBehavior.peekHeight = 370

        informationText = view.findViewById(R.id.tvHotspotInformation)
        totalSpeciesTextView = view.findViewById(R.id.tvNumOfSpecies)

        // Binding linearView
        val bottomSheetLayout = view.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)

        // Update heading text
        arguments?.getString(ARG_HEADING_TEXT)?.let {
            setBottomSheetHeadingText(it)
        }

        // Display the sightings in the bottom sheet
    }
    fun updateHotspotSightings(sightings: List<SightingModel>) {
        // Update the content of the bottom sheet with the received hotspot sightings
        val bottomSheetLayout = view?.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)
        bottomSheetLayout?.let { displaySightingsInBottomSheet(it, sightings) }
    }
    //==============================================================================================
    // Dynamically displays hotspot data
    @SuppressLint("InflateParams")
    fun displaySightingsInBottomSheet(
        bottomSheetView: LinearLayout,
        sightings: List<SightingModel>
    ) {
        val inflater = LayoutInflater.from(bottomSheetView.context)

        val filteredObservations = ToolBox.usersObservations.filter { observation ->
            observation.IsAtHotspot && areLocationsWithinDistance(
                arguments?.getDouble(ARG_LAT)!!,
                arguments?.getDouble(ARG_LON)!!,
                observation.Location.latitude,
                observation.Location.longitude,
                0.1
            )
        }

        val convertedSightings: List<SightingModel> = filteredObservations.map { userObs ->
            SightingModel(
                commonName = userObs.BirdName,
                howMany = userObs.Amount,
                date = userObs.Date,
                lat = userObs.Location.latitude,
                lng = userObs.Location.longitude
            )
        }

        if (sightings.isEmpty() && convertedSightings.isEmpty()) {
            totalSpeciesTextView.text = "No Species were found here"
            informationText.isVisible = false
        } else {

            for (userObs in convertedSightings) {
                val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                layoutParams.setMargins(0, 16, 0, 0)
                hotspotSightingView.layoutParams = layoutParams

                val commonNameTextView =
                    hotspotSightingView.findViewById<TextView>(R.id.tvCommonName)
                val howManyTextView = hotspotSightingView.findViewById<TextView>(R.id.tvHowMany)
                val dateTextView = hotspotSightingView.findViewById<TextView>(R.id.tvDate)
                val isUserObs = hotspotSightingView.findViewById<TextView>(R.id.tvIsUserObs)

                val commonNameText = "Common Name: "
                val italicCommonName = SpannableString(userObs.commonName)
                italicCommonName.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    0,
                    italicCommonName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Combine "Common Name: " and italicized common name
                val spannableCombined =
                    SpannableStringBuilder().append(commonNameText).append(italicCommonName)

                // Set the sighting information in the included layout
                commonNameTextView.text = spannableCombined
                howManyTextView.text = "How Many: ${userObs.howMany}"
                dateTextView.text = "Date: ${userObs.date}"

                isUserObs.visibility = View.VISIBLE

                bottomSheetView.addView(hotspotSightingView)
            }


            // Set number of species text
            totalSpeciesTextView.text = "${sightings.count()} species"

            // For every sighting
            for (sighting in sightings) {

                val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)

                // Set margins for layout
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 16, 0, 0)
                hotspotSightingView.layoutParams = layoutParams

                // TextViews
                val commonNameTextView =
                    hotspotSightingView.findViewById<TextView>(R.id.tvCommonName)
                val howManyTextView = hotspotSightingView.findViewById<TextView>(R.id.tvHowMany)
                val dateTextView = hotspotSightingView.findViewById<TextView>(R.id.tvDate)

                val commonNameText = "Common Name: "
                val italicCommonName = SpannableString(sighting.commonName)
                italicCommonName.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    0,
                    italicCommonName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Combine "Common Name: " and italicized common name
                val spannableCombined =
                    SpannableStringBuilder().append(commonNameText).append(italicCommonName)

                // Set the sighting information in the included layout
                commonNameTextView.text = spannableCombined
                howManyTextView.text = "How Many: ${sighting.howMany}"
                dateTextView.text = "Date: ${sighting.date}"

                hotspotSightingView.setOnClickListener() {
                    val intent = Intent(requireContext(), Navigation::class.java)
                    intent.putExtra("LATITUDE", ToolBox.currentLat)
                    intent.putExtra("LONGITUDE", ToolBox.currentLng)
                    intent.putExtra("DEST_LAT", ToolBox.destlat)
                    intent.putExtra("DEST_LNG", ToolBox.destlng)

                    startActivity(intent)

                    buttonClickListener?.invoke()
                }

                // Add the included layout to the bottom sheet
                bottomSheetView.addView(hotspotSightingView)
            }
        }
    }

    //Source: ChatGPT
    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Earth radius in kilometers

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }

    fun areLocationsWithinDistance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double,
        distanceKm: Double
    ): Boolean {
        val distance = haversine(
            lat1,
            lon1,
            lat2,
            lon2
        )

        return distance <= distanceKm
    }

    //==============================================================================================
    fun setBottomSheetHeadingText(newText: String) {
        val textView = view?.findViewById<TextView>(R.id.tvBottomSheetHeading)
        textView?.text = newText
    }

    //==============================================================================================
    // Button click listener for button on sheet
    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }
}
