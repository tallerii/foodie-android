package com.ruitzei.foodie.service

import com.ruitzei.foodie.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ruitzei on 3/17/18.
 */
interface ApiCalls {
    @GET("clients/")
    fun users(
            @Query("name") name: String?
    ): Call<List<String>>

    @POST("token-auth/username/")
    fun login(
        @Body loginData: LoginPostData
    ): Call<LoginResponse>

    @POST("token-auth/facebook/")
    fun loginWithFacebook(
        @Body loginData: FacebookLoginPostData
    ): Call<LoginResponse>

    @GET("clients/{id}/")
    fun getUserData(
        @Path("id") userId: String
    ): Call<User>

    @GET("clients/self/")
    fun getLoggedInUserData(): Call<User>

    @PATCH("clients/self/")
    fun updateLatLong(@Body latLong: LatLong): Call<User>

    @PATCH("clients/self/")
    fun updateUserData(@Body userDataUpdatePostData: UserDataUpdatePostData): Call<User>
}