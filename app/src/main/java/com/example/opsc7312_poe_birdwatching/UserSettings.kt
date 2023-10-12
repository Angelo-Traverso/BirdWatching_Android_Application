package com.example.opsc7312_poe_birdwatching

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.android.material.slider.RangeSlider

class UserSettings : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        //---SLIDER---
        val sliderDistance = view.findViewById<RangeSlider>(R.id.sliderDistance)
        val tvSliderText = view.findViewById<TextView>(R.id.tvMaxRadius)

        sliderDistance.setValues(ToolBox.user.MaxDistance.toFloat())

        // Set the track color
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))
        // Set the track color
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))

        // Set the thumb color
        sliderDistance.thumbTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))
        // Set the thumb color
        sliderDistance.thumbTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))

        sliderDistance.addOnChangeListener { slider, value, fromUser ->
            val displayValue = "$value Km"
            tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
            ToolBox.user.MaxDistance = value.toDouble()
        }


        return view
    }
}