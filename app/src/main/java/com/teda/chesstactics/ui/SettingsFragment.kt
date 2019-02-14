package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.teda.chesstactics.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.fragment_configuration)
    }

}