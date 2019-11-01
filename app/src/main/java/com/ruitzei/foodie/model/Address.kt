package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

//class Address (
//    @SerializedName("addresss") val address: String = "",
//    @SerializedName("location") val point: Point? = null
//)
//
//class Point(
//    @SerializedName("type") val type: String = "Point",
//    @SerializedName("coordinates") val coordinates: List<Double>
//)

class Address(
    @SerializedName("type") val type: String = "Point",
    @SerializedName("coordinates") val coordinates: List<Double>
)