package com.example.opsc7312_poe_birdwatching

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapterSettings(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MyObservations()
            1 -> UserSettings()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 2 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "My Observations"
            1 -> "Settings"
            else -> null
        }
    }
}