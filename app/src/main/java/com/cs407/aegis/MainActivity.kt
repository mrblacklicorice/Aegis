package com.cs407.aegis

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var holdImageButton: ImageButton
    private lateinit var circularProgressBar : ProgressBar
    private lateinit var tap_msg: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var incrementValue = 1
    private val MAX_VALUE = 100
    private val delay = 50L


    private val incrementRunnable = object : Runnable {
        override fun run() {
            if (incrementValue < MAX_VALUE) {
                incrementValue++

                circularProgressBar.progress = incrementValue

                handler.postDelayed(this, delay)
            } else {
                handler.removeCallbacks(this)

                // Trigger the panic function
                checkSMSPermission()
            }
        }
    }

    private val drainRunnable = object : Runnable {
        override fun run() {
            if (incrementValue > 0) {
                incrementValue -= 2

                circularProgressBar.progress = incrementValue

                handler.postDelayed(this, 10)
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ContactUtils.REQUEST_CODE_CONTACT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContactUtils.readContacts(contentResolver)
            } else {
                // Handle the case where the user denies the permissions
            }
        }
    }

    private fun checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        } else {
            // Permission has already been granted, trigger the panic function
            PanicUtils(this@MainActivity).panic()
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

        ContactUtils.checkAndRequestPermissions(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val tap_custom_message = sharedPreferences.getString("tap_custom_message", "Default Name")

        tap_msg = findViewById(R.id.tapLabel)
        tap_msg.text = tap_custom_message

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
