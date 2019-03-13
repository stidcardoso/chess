package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.fragment_categories.*

class ConfigurationFragment() : Fragment() {

    companion object {
        const val TAG = "CONFIGURATION_FRAGMENT"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.container, SettingsFragment())
        ft.commit()
    }

}