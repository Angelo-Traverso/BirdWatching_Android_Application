package com.example.opsc7312_poe_birdwatching

import PagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)

        val pagerAdapter = PagerAdapterSettings(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}