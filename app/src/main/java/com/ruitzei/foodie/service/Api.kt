package com.ruitzei.foodie.service

import com.ruitzei.foodie.application.FoodieApplication
import com.ruitzei.foodie.model.LoginPostData
import com.ruitzei.foodie.model.LoginResponse
import com.ruitzei.foodie.model.User
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

    fun getUserData(userId: String, listener: RequestCallbacks<User>) {
        val request = object: AbstractRequest<User>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.getUserData(userId), listener)
    }
}