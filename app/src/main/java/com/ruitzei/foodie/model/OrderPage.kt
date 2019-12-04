package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class OrderPage (
    @SerializedName("results") val results: List<OrderProperties>? = null
) {
    fun getOrders(): List<OrderProperties> = results.orEmpty()
}



class Results (
    @SerializedName("features") val orders: List<Order> = emptyList()
)