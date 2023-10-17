//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Settings : AppCompatActivity() {

    private lateinit var Logout: TextView
    private lateinit var Back: TextView

    //==============================================================================================
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

        Logout.setOnClickListener() {
            ToolBox.userID = -1
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Back.setOnClickListener() {
            val intent = Intent(this, Hotpots::class.java)
            startActivity(intent)
        }
    }

    //==============================================================================================
    override fun onBackPressed() {
        val intent = Intent(this, Hotpots::class.java)
        startActivity(intent)
    }
}