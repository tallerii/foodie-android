package com.ruitzei.foodie.model

import android.Manifest
import com.ruitzei.foodie.R

class LocationPermission(): AbstractPermission() {
    override val permissions: List<String>
        get() = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override val rationaleMessage: Int
        get() = R.string.location_text
}