package com.cs407.aegis

import android.content.Context
import android.content.SharedPreferences
import android.telephony.SmsManager
import androidx.preference.PreferenceManager

class PanicUtils(private val context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    public fun panic() {
        val phoneNumber = prefs.getString("emergency_contact_1", null)
        val customMessage = prefs.getString("custom_message", "I need help!")

        phoneNumber?.let { number ->
            customMessage?.let { message ->
                sendMessage(number, message)
            }
        }
    }

    private fun sendMessage(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}
