package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class Address (
    @SerializedName("addresss") val address: String = "",
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("long") val long: Double = 0.0
)