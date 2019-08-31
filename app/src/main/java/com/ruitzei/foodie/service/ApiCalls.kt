package com.ruitzei.foodie.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ruitzei on 3/17/18.
 */
interface ApiCalls {
    @GET("api/v1/test")
    fun testEndpoint(
            @Query("name") name: String?
    ): Call<List<String>>
}