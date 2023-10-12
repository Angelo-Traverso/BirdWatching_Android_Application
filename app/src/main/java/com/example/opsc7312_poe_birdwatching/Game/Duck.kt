package com.example.opsc7312_poe_birdwatching.Game

data class Duck(var x: Float, var y: Float, var speed: Int, var direction: String = "R", var isAlive: Boolean = true) {

    // Update the duck's position based on its speed and direction
    fun updatePosition() {
        if (direction == "R" && isAlive == true) {
            x += speed
        } else if (direction == "L" && isAlive == true) {
            x -= speed
        }
    }
}
