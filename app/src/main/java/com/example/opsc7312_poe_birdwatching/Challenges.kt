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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.Challenge_Object
import java.time.LocalDate
import java.util.*
import java.text.SimpleDateFormat

class Challenges : Fragment() {
    private var challengeList: List<Challenge_Object> = mutableListOf()

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the correct layout for this fragment
        val view = inflater.inflate(R.layout.fragment_challenges, container, false)

        // Populate challenges (Assuming you have this list populated somehow)
        challengeList = checkProgress()

        val linearLayout =
            view.findViewById<LinearLayout>(R.id.fragment_container) // Change to the correct ID

        val tvPoints = view.findViewById<TextView>(R.id.tvPoints)

        var totalPoints = 0;

        // Loop through the challenges and dynamically add them to the container
        for ((i, challenge) in challengeList.withIndex()) {

            // Instantiating challenge layout
            val challengeItemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.challenge_item_layout, null) // Change to the correct layout

            // Binding points to get view
            val pointSToGet = challengeItemView.findViewById<TextView>(R.id.tvPointsToGet)

            // Binding Challenge description view
            val tvChallengeDescription =
                challengeItemView.findViewById<TextView>(R.id.tvChallengeDescription)

            // Binding progress bar
            val progressBar = challengeItemView.findViewById<ProgressBar>(R.id.progressBar)

            // Binding progress view
            val tvProgress = challengeItemView.findViewById<TextView>(R.id.tvProgress)

            // Send user back
            val backTextView: TextView = view.findViewById(R.id.tvBack)
            backTextView.setOnClickListener {
                activity?.onBackPressed()
            }

            pointSToGet.text = "+${challenge.pointsToGet} points"

            tvChallengeDescription.text = challenge.description

            progressBar.max = challenge.required

            progressBar.progress = challenge.progress

            tvProgress.text = "${challenge.progress}/${challenge.required}"

            if (challenge.progress == challenge.required) {
                // Challenge completed
                totalPoints += challenge.pointsToGet
            }

            // Set top margin
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 64
            challengeItemView.layoutParams = params

            linearLayout.addView(challengeItemView) // Add the challenge item to the container
        }

        clearListAtMidnight()

        // Setting total points earned
        tvPoints.text = totalPoints.toString()
        return view
    }

    //==============================================================================================
    //create the challenges and check the users progress
    private fun checkProgress(): List<Challenge_Object> {
        val challenges = mutableListOf<Challenge_Object>()

        //spot 3 birds
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = Date()

        val filteredObservations = ToolBox.usersObservations.filter {
            it.UserID == ToolBox.userID && sdf.format(it.Date) == sdf.format(currentDate)
        }

        val uniqueBirdNames = filteredObservations.distinctBy { it.BirdName }
        val uniqueBirdCount = uniqueBirdNames.size

        challenges.add(Challenge_Object("Spot three bird species", uniqueBirdCount, 3, 15))

        //travel to two hotspots
        challenges.add(Challenge_Object("Travel to two hotspots", ToolBox.tripsCompleted, 2, 2))

        //duck hunt level
        challenges.add(
            Challenge_Object(
                "Reach the 7th round in duck hunt", ToolBox.topRoundInDuckHunt, 7, 10
            )
        )
        return challenges
    }

    //==============================================================================================
    //source: ChatGPT
    //method to reset challenges every day at midnight
    fun clearListAtMidnight() {
        val timer = Timer()
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance()

        // Set the time to midnight of the next day
        midnight.time = now.time
        midnight.add(Calendar.DAY_OF_YEAR, 1)
        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        midnight.set(Calendar.MILLISECOND, 0)

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                ToolBox.topRoundInDuckHunt = 0
                ToolBox.tripsCompleted = 0
                for (challenge in challengeList) {
                    challenge.progress = 0
                }
                println("List cleared at midnight.")
            }
        }, midnight.time, 24 * 60 * 60 * 1000) // Run every 24 hours
    }
}