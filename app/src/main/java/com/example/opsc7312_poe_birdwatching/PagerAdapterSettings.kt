package com.example.opsc7312_poe_birdwatching

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapterSettings(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> MyObservations()
            0 -> UserSettings()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    // Number of tabs
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Settings"
            1 -> "My Observations"
            else -> null
        }
    }
}