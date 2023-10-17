package com.example.opsc7312_poe_birdwatching

import PagerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.opsc7312_poe_birdwatching.Game.GameActivity
import com.google.android.material.tabs.TabLayout

class Settings : AppCompatActivity() {

    private lateinit var Logout: TextView
    private lateinit var Back: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        Logout = findViewById(R.id.tvLogout)
        Back = findViewById(R.id.tvBackSettings)

        val pagerAdapter = PagerAdapterSettings(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        Logout.setOnClickListener()
        {
            ToolBox.userID = -1
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Back.setOnClickListener()
        {
            val intent = Intent(this, Hotpots::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, Hotpots::class.java)
        startActivity(intent)
    }
}