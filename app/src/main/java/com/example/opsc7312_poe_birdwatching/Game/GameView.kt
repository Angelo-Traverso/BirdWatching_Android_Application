package com.example.opsc7312_poe_birdwatching.Game

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import com.example.opsc7312_poe_birdwatching.Hotpots
import com.example.opsc7312_poe_birdwatching.R
import java.util.*

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), Runnable {
    private val appContext: Context = context.applicationContext

    private var surfaceHolder: SurfaceHolder = holder
    private var isPlaying: Boolean = false
    private var thread: Thread? = null
    private val ducks: MutableList<Duck> = mutableListOf()
    private val hearts: MutableList<Heart> = mutableListOf()
    private val duckImageR: Bitmap
    private val duckImageL: Bitmap
    private var lives: Int = 3
    private var level: Int = 1
    private val pistol: Bitmap
    private val blood: Bitmap
    private val gunshot: Bitmap
    private val heartImage: Bitmap
    private val pauseButton: RectF = RectF(0F, 0F, 0F, 0F)

    init {
        ducks.add(Duck(0F, 100F, 30, "R", true))
        ducks.add(Duck(1000F, 100F, 40, "L", true))

        val scale = 0.2f

        // Load origional duck image duck image, set a resize size, then resize and save
        val originalDuckImage = BitmapFactory.decodeResource(resources, R.drawable.duckwingsup)
        duckImageR = Bitmap.createScaledBitmap(
            originalDuckImage,
            (originalDuckImage.width * scale).toInt(),
            (originalDuckImage.height * scale).toInt(),
            false
        )

        val matrix = Matrix()
        matrix.setScale(-scale, scale)
        duckImageL = Bitmap.createBitmap(
            originalDuckImage,
            0,
            0,
            originalDuckImage.width,
            originalDuckImage.height,
            matrix,
            false
        )

        val origionalPistol = BitmapFactory.decodeResource(resources, R.drawable.gun_transparent)
        pistol = Bitmap.createScaledBitmap(
            origionalPistol,
            (origionalPistol.width * scale).toInt(),
            (origionalPistol.height * scale).toInt(),
            false
        )

        val origionalBlood = BitmapFactory.decodeResource(resources, R.drawable.blood)
        blood = Bitmap.createScaledBitmap(
            origionalBlood,
            (origionalBlood.width * scale).toInt(),
            (origionalBlood.height * scale).toInt(),
            false
        )

        val origionalGunshot = BitmapFactory.decodeResource(resources, R.drawable.gunshot)
        gunshot = Bitmap.createScaledBitmap(
            origionalGunshot,
            (origionalGunshot.width * scale).toInt(),
            (origionalGunshot.height * scale).toInt(),
            false
        )

        val origionalheart = BitmapFactory.decodeResource(resources, R.drawable.heart)
        heartImage = Bitmap.createScaledBitmap(
            origionalheart,
            (origionalheart.width * 0.1).toInt(),
            (origionalheart.height * 0.1).toInt(),
            false
        )

        hearts.add(Heart(0F, 1F))
        hearts.add(Heart(100F, 1F))
        hearts.add(Heart(200F, 1F))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun run() {
        while (isPlaying) {
            if (surfaceHolder.surface.isValid) {
                val canvas: Canvas = surfaceHolder.lockCanvas()

                if (ducks.isEmpty()) {
                    isPlaying = false
                    nextLevel(canvas)
                }

                canvas.drawBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.windowsxpwallpaperbliss
                    ), matrix, null
                )
                drawPistol(canvas)

                val ducksToRemove = mutableListOf<Duck>()

                for (duck in ducks) {
                    duck.updatePosition()
                    if (duck.x > canvas.width || duck.x < 0 - duckImageR.width - 1) {
                        ducksToRemove.add(duck)
                        looseLife()
                    } else if (!duck.isAlive) {
                        drawBlood(canvas, duck)
                        drawGunshot(canvas)
                        ducksToRemove.add(duck)
                    } else {
                        drawDuck(canvas, duck)
                    }
                }

                // Remove ducks that should be removed
                ducks.removeAll(ducksToRemove)

                for (heart in hearts) {
                    drawHeart(canvas, heart)
                }

                drawPauseButton(canvas)
                drawLevelDisplay(canvas)

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Check if the user tapped the pause/unpause button
            if (pauseButton.contains(event.x, event.y)) {
                showPausePopup()
                isPlaying = false
            } else {
                val tappedDuck = findTappedDuck(event.x, event.y)
                if (tappedDuck != null) {
                    tappedDuck.isAlive = false
                }
            }
        }
        return true
    }

    private fun findTappedDuck(touchX: Float, touchY: Float): Duck? {
        for (duck in ducks) {
            if (touchX >= duck.x && touchX <= duck.x + duckImageR.width && touchY >= duck.y && touchY <= duck.y + duckImageR.height) {
                return duck
            }
        }
        return null
    }

    private fun nextLevel(canvas: Canvas) {

        level++

        canvas.drawBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.windowsxpwallpaperbliss
            ), matrix, null
        )
        val random = Random()

        val width = duckImageR.width

        for (i in 1..level + 1) {
            val initialX = if (random.nextBoolean()) 0 - width else canvas.width - 1
            val initialY = random.nextInt(canvas.height - 900) + 100
            val direction = if (initialX == 0 - width) "R" else "L"
            val speed = random.nextInt(20) + 20
            ducks.add(Duck(initialX.toFloat(), initialY.toFloat(), speed, direction, true))
        }

        isPlaying = true

    }

    private fun looseLife() {
        lives--

        when (lives) {
            0 -> {
                hearts.removeAt(0)
                isPlaying = false
                post { showGameOverPopup() }
            }
            1 -> {
                hearts.removeAt(1)
            }
            2 -> {
                hearts.removeAt(2)
            }
            3 -> {

            }
            else -> {
                isPlaying = false
            }
        }
    }

    private fun showGameOverPopup() {
        val dialogView = View.inflate(context, R.layout.game_dialog_game_over, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()

        val retryButton = dialogView.findViewById<Button>(R.id.btnRetry)
        retryButton.setOnClickListener {
            restartGame()
            alertDialog.dismiss()
        }

        val exitButton = dialogView.findViewById<Button>(R.id.btnExitGameOver)
        exitButton.setOnClickListener {
            val intent = Intent(appContext, Hotpots::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }

        alertDialog.show()
    }

    private fun showPausePopup() {
        val dialogView = View.inflate(context, R.layout.game_dialog_pause, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()

        val resumeButton = dialogView.findViewById<Button>(R.id.btnResume)
        resumeButton.setOnClickListener {
            isPlaying = true
            resume()
            alertDialog.dismiss()
        }

        val exitButton = dialogView.findViewById<Button>(R.id.btnExit)
        exitButton.setOnClickListener {
            val intent = Intent(appContext, Hotpots::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }

        alertDialog.show()
    }

    private fun restartGame() {
        ducks.clear()
        hearts.clear()
        hearts.add(Heart(0F, 1F))
        hearts.add(Heart(100F, 1F))
        hearts.add(Heart(200F, 1F))
        lives = 3
        level = 0
        isPlaying = true
        resume()
    }

    private fun drawHeart(canvas: Canvas, heart: Heart) {
        canvas.drawBitmap(
            heartImage, heart.x, heart.y, null
        )
    }

    private fun drawDuck(canvas: Canvas, duck: Duck) {
        if (duck.direction == "R") canvas.drawBitmap(duckImageR, duck.x, duck.y, null)
        else canvas.drawBitmap(duckImageL, duck.x, duck.y, null)
    }

    private fun drawPistol(canvas: Canvas) {
        canvas.drawBitmap(
            pistol, (canvas.width / 2).toFloat(), (canvas.height - pistol.height).toFloat(), null
        )
    }

    private fun drawBlood(canvas: Canvas, duck: Duck) {
        canvas.drawBitmap(
            blood, (duck.x - duckImageL.width / 2), (duck.y - duckImageL.height / 2), null
        )
    }

    private fun drawGunshot(canvas: Canvas) {
        canvas.drawBitmap(
            gunshot,
            ((canvas.width / 2) + (gunshot.width / 2)).toFloat(),
            (canvas.height - pistol.height).toFloat(),
            null
        )
    }

    private fun drawPauseButton(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.TRANSPARENT
        paint.style = Paint.Style.FILL

        pauseButton.set(10F, (this.height - 60).toFloat(), 160F, (this.height - 10).toFloat())

        canvas.drawRect(pauseButton, paint)

        paint.color = Color.WHITE
        paint.textSize = 60F

        val buttonText = "Pause"

        canvas.drawText(
            buttonText,
            pauseButton.centerX() - paint.measureText(buttonText) / 2,
            pauseButton.centerY() + paint.textSize / 2,
            paint
        )
    }

    private fun drawLevelDisplay(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.WHITE
        val paintText = "Level: $level"
        paint.textSize = 60F

        canvas.drawText(
            paintText,
            (this.width - paint.measureText(paintText)),
            paint.textSize,
            paint
        )
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }
}