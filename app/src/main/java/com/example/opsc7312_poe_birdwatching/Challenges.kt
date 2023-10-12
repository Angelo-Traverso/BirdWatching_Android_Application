package com.example.opsc7312_poe_birdwatching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class Challenges : Fragment() {
    private lateinit var challengeList: List<Challenge_Object> // List of challenges

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the correct layout for this fragment
        val view = inflater.inflate(R.layout.fragment_challenges, container, false)

        // Populate challenges (Assuming you have this list populated somehow)
        challengeList = getChallenges()

        val linearLayout = view.findViewById<LinearLayout>(R.id.fragment_container) // Change to the correct ID

        // Loop through the challenges and dynamically add them to the container
        for ((i, challenge) in challengeList.withIndex()) {
            val challengeItemView = LayoutInflater.from(requireContext()).inflate(R.layout.challenge_item_layout, null) // Change to the correct layout

            val tvChallengeDescription = challengeItemView.findViewById<TextView>(R.id.tvChallengeDescription)
            val progressBar = challengeItemView.findViewById<ProgressBar>(R.id.progressBar)
            val tvProgress = challengeItemView.findViewById<TextView>(R.id.tvProgress)
            val tvPoints = challengeItemView.findViewById<TextView>(R.id.tvPoints)

            // Send user back
            val backTextView: TextView = view.findViewById(R.id.tvBack)
            backTextView.setOnClickListener {
                activity?.onBackPressed()
            }
            // Set the challenge details
            tvChallengeDescription.text = challenge.description
            progressBar.max = 100 // Set the maximum progress value (assuming it's a percentage)
            progressBar.progress = challenge.progress
            tvProgress.text = "${challenge.progress}/100" // Adjust as per your progress representation
            tvPoints.text = "+${challenge.pointsToGet} points"

            // Set top margin
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 64
            challengeItemView.layoutParams = params

            linearLayout.addView(challengeItemView) // Add the challenge item to the container
        }
        return view
    }

    private fun getChallenges(): List<Challenge_Object> {
        val challenges = mutableListOf<Challenge_Object>()

        // Assuming some challenges
        challenges.add(Challenge_Object("Find 3 new Species", 30, 10))
        challenges.add(Challenge_Object("Find 2 birds", 50, 15))
        challenges.add(Challenge_Object("Challenge 3", 75, 20))

        return challenges
    }
}