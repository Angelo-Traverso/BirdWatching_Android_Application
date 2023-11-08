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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserSettings : Fragment() {

    private lateinit var btnMetric: Button
    private lateinit var btnImperial: Button
    private lateinit var btnDark: Button
    private lateinit var btnLight: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvSliderText: TextView
    private lateinit var sliderDistance: RangeSlider

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)


        // TextViews
        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)

       tvUserName.text =
          "${ToolBox.users[0].Name} ${ToolBox.users[0].Surname}"

        //tvUserEmail.text = "${ToolBox.users[ToolBox.userID].Email}"

        //----SLIDER----
        sliderDistance = view.findViewById(R.id.sliderDistance)
        tvSliderText = view.findViewById(R.id.tvMaxRadius)

        //set slider to users preference
        sliderDistance.setValues(ToolBox.users[0].MaxDistance.toFloat())

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

            if (!ToolBox.users[0].isUnitKM) {
                unit = "mile"
            }

            val displayValue = "$value $unit"
            tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
            ToolBox.users[0].MaxDistance = value.toDouble()

            updateUserSettings()
        }

        //---Hotspot Map Style---
        btnDark = view.findViewById(R.id.btnDark)
        btnLight = view.findViewById(R.id.btnLight)

        if (ToolBox.users[0].mapStyleIsDark) {
            toDark()
        } else {
            toLight()
        }

        btnDark.setOnClickListener {
            toDark()
        }

        btnLight.setOnClickListener {
            toLight()
        }

        //---MEASUREMENT UNITS---
        btnMetric = view.findViewById(R.id.btnMetric)
        btnImperial = view.findViewById(R.id.btnImperial)

        if (ToolBox.users[0].isUnitKM == true) {
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

        ToolBox.users[0].isUnitKM = true

        var value = ToolBox.users[0].MaxDistance.toFloat()
        var unit = "km"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"

        updateUserSettings()
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

        ToolBox.users[0].isUnitKM = false

        var value = ToolBox.users[0].MaxDistance.toFloat()
        var unit = "mile"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"

        updateUserSettings()

    }

    private fun toDark() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnDark, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnLight, unselectedColorStateList)

        ToolBox.users[0].mapStyleIsDark = true

        updateUserSettings()
    }

    private fun toLight() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnLight, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnDark, unselectedColorStateList)

        ToolBox.users[0].mapStyleIsDark = false

        updateUserSettings()
    }

    private fun updateUserSettings() {
        try {
            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            val user = ToolBox.users[0]

            val fbUser = Firebase.auth.currentUser

            if(fbUser != null) {
                usersCollection.document(fbUser.uid)
                    .update(
                        "isUnitKM", user.isUnitKM,
                        "mapStyleIsDark", user.mapStyleIsDark,
                        "MaxDistance", user.MaxDistance
                    )
                    .addOnSuccessListener {
                        // Settings updated in Firestore successfully
                        // You can show a toast message or handle success as needed
                        Toast.makeText(
                            requireContext(),
                            "User settings updated in Firestore",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        // Handle the error if updating data in Firestore fails
                        Toast.makeText(
                            requireContext(),
                            "Failed to update user settings in Firestore: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } catch (ex: Exception) {
            Log.e("log", "Error updating user settings in Firestore: ${ex.message}")
            ex.printStackTrace()
        }
    }


}