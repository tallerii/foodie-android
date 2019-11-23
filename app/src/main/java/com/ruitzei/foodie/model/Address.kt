package com.ruitzei.foodie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//class Address (
//    @SerializedName("addresss") val address: String = "",
//    @SerializedName("location") val point: Point? = null
//)
//
//class Point(
//    @SerializedName("type") val type: String = "Point",
//    @SerializedName("coordinates") val coordinates: List<Double>
//)

@Parcelize
class Address(
    @SerializedName("type") val type: String = "Point",
    @SerializedName("coordinates") val coordinates: List<Double>
): Parcelable