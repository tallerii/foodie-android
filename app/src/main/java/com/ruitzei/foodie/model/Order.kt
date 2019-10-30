package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class Order (
    @SerializedName("") var description: String = "",
    @SerializedName("") var amount: String = "",
    @SerializedName("") var addressFrom: Address? = null,
    @SerializedName("") var addressTo: Address? = null
)