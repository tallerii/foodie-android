package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class FacebookLoginPostData(
    @SerializedName("token") val token: String,
    @SerializedName("FCMToken") val FCMToken: String)