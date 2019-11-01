package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName
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

class Order (
    @SerializedName("id") var id: String = "",
    @SerializedName("properties") var properties: OrderProperties? = null
)

class OrderProperties (
    @SerializedName("notes") var notes: String = "",
    @SerializedName("date_time_ordered") var createdAt: Date? = null,
    @SerializedName("price") var price: String = "",
    @SerializedName("start_location") var startLocation: Address? = null,
    @SerializedName("end_location") var endLocation: Address? = null,
    @SerializedName("delivered") var delivered: Boolean = false,
    @SerializedName("delivery_user") var deliveryUser: String = ""
)

class OrderPostObject (
    @SerializedName("notes") var description: String = "",
    @SerializedName("price") var amount: String = "",
    @SerializedName("start_location") var addressFrom: Address? = null,
    @SerializedName("end_location") var addressTo: Address? = null
)