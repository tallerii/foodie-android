package com.ruitzei.foodie.service

import com.ruitzei.foodie.model.LatLong
import com.ruitzei.foodie.model.LoginPostData
import com.ruitzei.foodie.model.LoginResponse
import com.ruitzei.foodie.model.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ruitzei on 3/17/18.
 */
interface ApiCalls {
    @GET("clients   /")
    fun users(
            @Query("name") name: String?
    ): Call<List<String>>

    @POST("api-token-auth/")
    fun login(
        @Body loginData: LoginPostData
    ): Call<LoginResponse>

    @GET("clients/{id}/")
    fun getUserData(
        @Path("id") userId: String
    ): Call<User>

    @GET("clients/self/")
    fun getLoggedInUserData(): Call<User>

    @PATCH("clients/self/")
    fun updateLatLong(@Body latLong: LatLong): Call<User>
}