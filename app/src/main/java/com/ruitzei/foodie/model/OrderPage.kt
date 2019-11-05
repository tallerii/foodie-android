package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class OrderPage (
    @SerializedName("results") val results: Results? = null
) {
    fun getOrders(): List<Order> = results?.orders.orEmpty()
}



class Results (
    @SerializedName("features") val orders: List<Order> = emptyList()
)