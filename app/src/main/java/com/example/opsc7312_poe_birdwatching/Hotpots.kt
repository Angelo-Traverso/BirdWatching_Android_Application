package com.example.opsc7312_poe_birdwatching

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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Hotpots : AppCompatActivity() {


    private var isMenuVisible = false;
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fab1: FloatingActionButton
    private lateinit var fab2: FloatingActionButton
    private lateinit var fab3: FloatingActionButton
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
        ///
        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
        fabAnticlock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)

        fabMenu = findViewById(R.id.fabMenu)
        fab1 = findViewById(R.id.menu_item_1)
        fab2 = findViewById(R.id.menu_item_2)
        fab3 = findViewById(R.id.menu_item_3)
        fab4 = findViewById(R.id.menu_item_4)
        fab5 = findViewById(R.id.menu_item_5)
        val linearLayout = findViewById<LinearLayout>(R.id.linearAppBar)

        fabMenu.setOnClickListener {
            if (isOpen) {
                fab1.startAnimation(fabClose)
                fab2.startAnimation(fabClose)
                fab3.startAnimation(fabClose)
                fab4.startAnimation(fabClose)
                fab5.startAnimation(fabClose)
                fabMenu.startAnimation(fabAnticlock)
                isOpen = false
            } else {
                fabMenu.startAnimation(fabClock)
                fab1.startAnimation(fabOpen)
                fab2.startAnimation(fabOpen)
                fab3.startAnimation(fabOpen)
                fab4.startAnimation(fabOpen)
                fab5.startAnimation(fabOpen)
                isOpen = true
            }

            //showFabMenu(it)

          /*  if (!isFABOpen) {

                showFabMenu();
            } else {
                closeFABMenu();
            }*/
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Handle menu item click here
            when (menuItem.itemId) {
                R.id.menuGame -> {
                    // Handle menu item 1 click
                    true
                }
                R.id.menuSettings -> {
                    // Handle menu item 2 click
                    true
                }
                R.id.menuAddSighting -> {
                    // Handle menu item 2 click
                    true
                }
                R.id.menuViewSighting -> {
                    // Handle menu item 2 click
                    true
                }
                R.id.menuRewards -> {
                    // Handle menu item 2 click
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showFabMenu() {
        /*val popupView = LayoutInflater.from(this).inflate(R.layout.fab_menu_layout, null)
        val popupWindow = PopupWindow(
            popupView,
            300,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Calculate the y-offset to position the popup above the FAB
        val yOffset = -(popupWindow.height + anchor.height)

        // Example: Set actions for menu items
        val menuItem1 = popupView.findViewById<ImageButton>(R.id.menu_item_1)
        menuItem1.setOnClickListener {
            // Handle menu item 1 click
            // Add your logic here
            popupWindow.dismiss()
        }

        val menuItem2 = popupView.findViewById<ImageButton>(R.id.menu_item_2)
        menuItem2.setOnClickListener {
            // Handle menu item 2 click
            // Add your logic here
            popupWindow.dismiss()
        }

        // Show the popup menu above the FAB
        popupWindow.showAsDropDown(anchor, 0, yOffset)*/

        isFABOpen = true

        fabMenu.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_155))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_205))
        fab4.animate().translationY(-resources.getDimension(R.dimen.standard_255))
        fab5.animate().translationY(-resources.getDimension(R.dimen.standard_305))


    }

    private fun closeFABMenu() {
        isFABOpen=false
        val fabMenuTranslationY = fabMenu.translationY

        fab1.animate().translationY(fabMenuTranslationY)
        fab2.animate().translationY(fabMenuTranslationY)
        fab3.animate().translationY(fabMenuTranslationY)
        fab4.animate().translationY(fabMenuTranslationY)
        fab5.animate().translationY(fabMenuTranslationY)

    }

}