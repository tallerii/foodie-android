package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class UpdateLocationModel (
    @SerializedName("last_location") val geometry: Address
)