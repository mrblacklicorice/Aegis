package com.cs407.aegis

import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}

