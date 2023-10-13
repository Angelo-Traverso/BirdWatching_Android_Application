package com.example.opsc7312_poe_birdwatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHotspot : BottomSheetDialogFragment() {

    private var buttonClickListener: (() -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_hotspot, container, false)

        // Find the button and set a click listener
        val startNavigationButton = view.findViewById<Button>(R.id.btnStartNavigation)
        startNavigationButton.setOnClickListener {
            // Notify the listener when the button is clicked
            buttonClickListener?.invoke()
        }
        return view

    }
    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }
}