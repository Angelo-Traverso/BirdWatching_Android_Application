//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.UserObservation

class MyObservations : Fragment() {

    private lateinit var llObservationContainer: LinearLayout

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_observations, container, false)

        llObservationContainer = view.findViewById(R.id.myObservationContainer)

        val filteredObservations = ToolBox.usersObservations.filter { it.UserID == ToolBox.users[0].UserID }
        // Add a view for every element
        for (observation in filteredObservations) {
            addObservationViewToContainer(observation)
        }
        return view
    }

    //==============================================================================================
    //  Function Adds new view to container for every userObservation instance
    private fun addObservationViewToContainer(userObservation: UserObservation) {
        val inflater = LayoutInflater.from(requireContext())
        val observationView = inflater.inflate(R.layout.my_observations_display_layout, null)
        val line = inflater.inflate(R.layout.line, null)
        val worker = APIWorker()

        // Populate the fields with data from UserObservation
        observationView.findViewById<TextView>(R.id.tvBirdName).text =
            userObservation.BirdName + " (x${userObservation.Amount})"

        // Displaying location - should display location normal name
        observationView.findViewById<TextView>(R.id.tvLocation).text =
            userObservation.PlaceName + "\n" + userObservation.Location.longitude.toString() + " " + userObservation.Location.latitude.toString()
        observationView.findViewById<TextView>(R.id.tvDateSpotted).text =
            userObservation.Date.toString()

        if (userObservation.Note.isNotEmpty()) {
            observationView.findViewById<TextView>(R.id.tvViewObsNote).text = userObservation.Note
        } else {
            observationView.findViewById<TextView>(R.id.tvViewObsNote).isVisible = false
        }

        // Add the inflated custom view to the linear layout
        llObservationContainer.addView(observationView)
        llObservationContainer.addView(line)
    }
}