package com.ruitzei.foodie.service

import com.ruitzei.foodie.application.FoodieApplication
import com.ruitzei.foodie.model.*
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK

/**
 * Created by ruitzei on 3/17/18.
 */
object Api {

    private var apiCalls: ApiCalls? = null

    init {
        apiCalls = FoodieApplication.instance!!.api!!
    }

    fun performLogin(username: String, password: String, listener: RequestCallbacks<LoginResponse>) {
        val request = object: AbstractRequest<LoginResponse>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.login(LoginPostData( username, password)), listener)
    }

    fun performFacebookLogin(fbToken: String, listener: RequestCallbacks<LoginResponse>) {
        val request = object: AbstractRequest<LoginResponse>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.loginWithFacebook(FacebookLoginPostData(fbToken)), listener)
    }

    fun getUserData(userId: String, listener: RequestCallbacks<User>) {
        val request = object: AbstractRequest<User>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getUserData(userId), listener)
    }

    fun getLoggedInUserData(listener: RequestCallbacks<User>) {
        val request = object: AbstractRequest<User>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getLoggedInUserData(), listener)
    }

    fun updateLatLong(latLong: LatLong, listener: RequestCallbacks<User>) {
        val request = object: AbstractRequest<User>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        val address = Address(
            coordinates = listOf(latLong.lat, latLong.lon)
        )
        request.enqueue(apiCalls!!.updateLatLong(UpdateLocationModel(address)), listener)
    }

    fun updateUserData(name: String, phoneNumber: String, mail: String, listener: RequestCallbacks<User>) {
        val request = object: AbstractRequest<User>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.updateUserData(UserDataUpdatePostData(name, phoneNumber, mail)), listener)
    }

    fun createOrder(order: OrderPostObject, listener: RequestCallbacks<Order>) {
        val request = object: AbstractRequest<Order>() {
            override val successCode: Int
                get() = HTTP_CREATED
        }

        request.enqueue(apiCalls!!.createOrder(order), listener)
    }

    fun getOrders(listener: RequestCallbacks<OrderPage>) {
        val request = object: AbstractRequest<OrderPage>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getOrders(), listener)
    }

    fun getActiveOrders(listener: RequestCallbacks<OrderPage>) {
        val request = object: AbstractRequest<OrderPage>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getActiveOrders(), listener)
    }

    fun getUnassignedOrders(listener: RequestCallbacks<OrderPage>) {
        val request = object: AbstractRequest<OrderPage>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getUnassignedOrders(), listener)
    }
}