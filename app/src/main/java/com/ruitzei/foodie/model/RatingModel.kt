package com.ruitzei.foodie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/*
    "notes": "",
    "value": null,
    "order": null,
    "user": null
 */
@Parcelize
class RatingModel(
    @SerializedName("notes") val notes: String = "",
    @SerializedName("value") val value: Int = 1,
    @SerializedName("order") val order: String = "",
    @SerializedName("user") val user: String = ""

): Parcelable {
    companion object {
        val TAG: String = RatingModel::class.java.simpleName
    }
}