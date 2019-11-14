package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class LatLong(
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lon") val lon: Double = 0.0
)