package com.example.opsc7312_poe_birdwatching

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style




class Hotpots : AppCompatActivity() {


    private var isMenuVisible = false;
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fab1: FloatingActionButton
    private lateinit var settings: FloatingActionButton
    private lateinit var addObservation: FloatingActionButton
    private lateinit var fab4: FloatingActionButton
    private lateinit var fab5: FloatingActionButton
    private var isFABOpen = false


    ///

    private lateinit var fabClose: Animation
    private lateinit var fabOpen: Animation
    private lateinit var fabClock: Animation
    private lateinit var fabAnticlock: Animation
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hotpots)

        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()


        val fragment = map()
        transaction.replace(R.id.mainContainer, fragment)

        transaction.addToBackStack(null)

        transaction.commit()



        ///
        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
        fabAnticlock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)

        fabMenu = findViewById(R.id.fabMenu)
        fab1 = findViewById(R.id.menu_item_1)
        settings = findViewById(R.id.menu_item_2)
        addObservation = findViewById(R.id.menu_item_3)
        fab4 = findViewById(R.id.menu_item_4)
        fab5 = findViewById(R.id.menu_item_5)
        val linearLayout = findViewById<LinearLayout>(R.id.linearAppBar)

        addObservation.setOnClickListener{
            val intent = Intent(this, AddObservation::class.java )
            startActivity(intent)

            // Close the menu when item clicked
            close()

        }

        settings.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)

            // Close the menu when item clicked
            close()

        }
        fab5.setOnClickListener{
            loadChallengesFragment()

            // Close the menu when item clicked
            close()
        }
        fabMenu.setOnClickListener {
            if(isOpen())
            {
                close()
            }else
            {

                open()
            }
        }
    }

    private fun open()
    {
        fabMenu.startAnimation(fabClock)
        fab1.startAnimation(fabOpen)
        settings.startAnimation(fabOpen)
        addObservation.startAnimation(fabOpen)
        fab4.startAnimation(fabOpen)
        fab5.startAnimation(fabOpen)
        isOpen = true

    }

    private fun isOpen() : Boolean
    {
        if (isOpen) {
            fab1.startAnimation(fabClose)
            settings.startAnimation(fabClose)
            addObservation.startAnimation(fabClose)
            fab4.startAnimation(fabClose)
            fab5.startAnimation(fabClose)
            fabMenu.startAnimation(fabAnticlock)
            return true

        } else {
            fabMenu.startAnimation(fabClock)
            fab1.startAnimation(fabOpen)
            settings.startAnimation(fabOpen)
            addObservation.startAnimation(fabOpen)
            fab4.startAnimation(fabOpen)
            fab5.startAnimation(fabOpen)
            return false
        }

    }

    private fun close()
    {
        fab1.startAnimation(fabClose)
        settings.startAnimation(fabClose)
        addObservation.startAnimation(fabClose)
        fab4.startAnimation(fabClose)
        fab5.startAnimation(fabClose)
        fabMenu.startAnimation(fabAnticlock)
        isOpen = false

    }

    private fun loadChallengesFragment() {
        val challengesFragment = Challenges()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, challengesFragment)
            .addToBackStack(null) // If you want to allow back navigation
            .commit()
    }
}