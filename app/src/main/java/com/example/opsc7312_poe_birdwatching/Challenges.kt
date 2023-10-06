package com.example.opsc7312_poe_birdwatching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Challenges.newInstance] factory method to
 * create an instance of this fragment.
 */
class Challenges : Fragment() {
    private lateinit var challengeList: List<Challenge_Object> // List of challenges

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Challenges.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Challenges().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}