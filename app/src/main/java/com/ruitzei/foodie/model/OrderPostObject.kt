package com.ruitzei.foodie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/*
{
    "id": "9dd55a02-71d8-4a72-b01e-f6e911b390a4",
    "type": "Feature",
    "geometry": null,
    "properties": {
        "notes": null,
        "delivery_user": null,
        "client_user": "36e1ace4-e58e-4d66-aa67-c39a9158c843",
        "delivered": false,
        "date_time_ordered": "2019-10-31T20:22:59+0000",
        "price": null,
        "start_location": null,
        "end_location": null
    }
}
 */

@Parcelize
class Order (
    @SerializedName("id") var id: String = "",
    @SerializedName("properties") var properties: OrderProperties? = null
): Parcelable

@Parcelize
class OrderProperties (
    @SerializedName("id") var id: String = "",
    @SerializedName("notes") var notes: String = "",
    @SerializedName("date_time_ordered") var createdAt: Date? = null,
    @SerializedName("price") var price: Double = 0.0,
    @SerializedName("delivery_price") var deliveryPrice: Double = 0.0,
    @SerializedName("start_location") var startLocation: Address? = null,
    @SerializedName("end_location") var endLocation: Address? = null,
    @SerializedName("delivered") var delivered: Boolean = false,
    @SerializedName("delivery_user") var deliveryUser: User? = null,
    @SerializedName("client_user") var clientUser: User? = null
): Parcelable

class OrderPostObject (
    @SerializedName("notes") var description: String = "",
    @SerializedName("price") var amount: String = "",
    @SerializedName("start_location") var addressFrom: Address? = null,
    @SerializedName("end_location") var addressTo: Address? = null
)