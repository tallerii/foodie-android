package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class UserDataUpdatePostData(
    @SerializedName("first_name") val name: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("mail") val mail: String
)