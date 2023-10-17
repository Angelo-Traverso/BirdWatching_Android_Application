//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider

class UserSettings : Fragment() {

    private lateinit var btnMetric: Button
    private lateinit var btnImperial: Button
    private lateinit var tvSliderText: TextView
    private lateinit var sliderDistance: RangeSlider

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        //----SLIDER----
        sliderDistance = view.findViewById(R.id.sliderDistance)
        tvSliderText = view.findViewById(R.id.tvMaxRadius)

        //set slider to users preference
        sliderDistance.setValues(ToolBox.users[ToolBox.userID].MaxDistance.toFloat())

        // Set the track color
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))

        // Set tick color
        sliderDistance.tickTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))

        // Set the track color
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))

        // Set the thumb color
        sliderDistance.thumbTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))

        sliderDistance.addOnChangeListener { slider, value, fromUser ->
            var unit = "km"

            if (!ToolBox.users[ToolBox.userID].isUnitKM)
            {
                unit = "mile"
            }

            val displayValue = "$value $unit"
            tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
            ToolBox.users[ToolBox.userID].MaxDistance = value.toDouble()
        }

        //---MEASUREMENT UNITS---
        btnMetric = view.findViewById<Button>(R.id.btnMetric)
        btnImperial = view.findViewById<Button>(R.id.btnImperial)

        if (ToolBox.users[ToolBox.userID].isUnitKM == true) {
            ToMetric()
        } else {
            ToImperial()
        }

        btnImperial.setOnClickListener() {
            ToImperial()
        }

        btnMetric.setOnClickListener() {
            ToMetric()
        }

        return view
    }

    //==============================================================================================
    //changes which button is highlighted, which text is on the slider, and the var in the user class
    private fun ToMetric() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnMetric, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnImperial, unselectedColorStateList)

        ToolBox.users[ToolBox.userID].isUnitKM = true

        var value = ToolBox.users[ToolBox.userID].MaxDistance.toFloat()
        var unit = "km"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
    }

    //==============================================================================================
    //changes which button is highlighted, which text is on the slider, and the var in the user class
    private fun ToImperial() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnImperial, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnMetric, unselectedColorStateList)

        ToolBox.users[ToolBox.userID].isUnitKM = false

        var value = ToolBox.users[ToolBox.userID].MaxDistance.toFloat()
        var unit = "mile"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
    }
}