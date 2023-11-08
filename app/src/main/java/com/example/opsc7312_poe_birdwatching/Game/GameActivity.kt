//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching.Game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.opsc7312_poe_birdwatching.ChallengeModel
import com.example.opsc7312_poe_birdwatching.Hotpots
import com.example.opsc7312_poe_birdwatching.R
import com.example.opsc7312_poe_birdwatching.ToolBox
import com.example.opsc7312_poe_birdwatching.databinding.ActivityGameBinding

//this is the main activity used to host the duck hunt game
class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameView: GameView
    private lateinit var startGame: TextView

    //==============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        gameView = binding.gameView
        startGame = binding.startGame

        view.setOnClickListener(){
            run()
        }
    }

    //==============================================================================================
    private fun run(){
        startGame.isVisible = false
        gameView.isVisible = true
        gameView.start()
    }

    //==============================================================================================
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        ChallengeModel.saveChallenge()
        gameView.stop()
        val intent = Intent(this, Hotpots::class.java)
        startActivity(intent)
    }
}