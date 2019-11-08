package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class PatchOrderWithUserData(
    @SerializedName("delivery_user") val deliveryId: String
)