package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class LoginPostData (@SerializedName("username") val username: String,
                     @SerializedName("password") val password: String)