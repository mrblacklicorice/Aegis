package com.cs407.aegis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var holdImageButton: ImageButton
    private lateinit var circularProgressBar : ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private var incrementValue = 1
    private val MAX_VALUE = 100
    private val delay = 50L



    private val incrementRunnable = object : Runnable {
        override fun run() {
            if (incrementValue < MAX_VALUE) {
                incrementValue++

//                update the UI with the new value
                circularProgressBar.progress = incrementValue

                handler.postDelayed(this, delay)
            }
        }
    }

    private val drainRunnable = object : Runnable {
        override fun run() {
            if (incrementValue > 0) {
                incrementValue -= 2

//                update the UI with the new value
                circularProgressBar.progress = incrementValue

                handler.postDelayed(this, 10)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        circularProgressBar = findViewById(R.id.circularProgressBar)
        circularProgressBar.progress = 0

        holdImageButton = findViewById(R.id.tapButton)
        holdImageButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.postDelayed(incrementRunnable, delay)
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    handler.removeCallbacks(incrementRunnable)
                    handler.postDelayed(drainRunnable, 5)

                    true
                }
                else -> false
            }
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val companionCallButton = findViewById<ImageButton>(R.id.callButton)
        companionCallButton.setOnClickListener {
            val intent = Intent(this, CompanionCallActivity::class.java)
            startActivity(intent)
        }

        val messageButton = findViewById<ImageButton>(R.id.messageButton)
        messageButton.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }

        val locationToggle = findViewById<ImageButton>(R.id.locationToggle)
        locationToggle.tag = R.drawable.baseline_location_off_24

        locationToggle.setOnClickListener {
            if (locationToggle.tag == R.drawable.baseline_location_off_24) {
                locationToggle.setImageResource(R.drawable.baseline_location_on_24)
                locationToggle.tag = R.drawable.baseline_location_on_24
            } else {
                locationToggle.setImageResource(R.drawable.baseline_location_off_24)
                locationToggle.tag = R.drawable.baseline_location_off_24
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Implement edge-to-edge as necessary, depending on Android version
    }
}
