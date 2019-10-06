package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class LoginResponse(@SerializedName("token") val token: String)