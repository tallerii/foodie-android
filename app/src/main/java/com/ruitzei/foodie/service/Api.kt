package com.ruitzei.foodie.service

import com.ruitzei.foodie.application.FoodieApplication
import java.net.HttpURLConnection.HTTP_OK

/**
 * Created by ruitzei on 3/17/18.
 */
object Api {

    private var apiCalls: ApiCalls? = null

    init {
        apiCalls = FoodieApplication.instance!!.api!!
    }

    fun testCall(listener: RequestCallbacks<List<String>>) {
        val request = object: AbstractRequest<List<String>>() {
            override val successCode: Int
                get() = HTTP_OK
        }

        request.enqueue(apiCalls!!.testEndpoint(""), listener)
    }
}