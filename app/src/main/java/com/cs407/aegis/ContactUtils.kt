package com.cs407.aegis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object ContactUtils {
    public const val REQUEST_CODE_CONTACT = 1

    fun checkAndRequestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.SEND_SMS),
                REQUEST_CODE_CONTACT
            )
        } else {
            // permissions are already granted
            readContacts(activity.contentResolver)
        }
    }

    @SuppressLint("Range")
    fun readContacts(contentResolver: ContentResolver) {
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                // Here you might store the contact info or use it
                sendMessage(phoneNumber, "Your message here", contentResolver)
            }
        }
    }

    fun sendMessage(phoneNumber: String, message: String, contentResolver: ContentResolver) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}
