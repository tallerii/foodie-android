package com.ruitzei.foodie.service

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by ruitzei on 3/17/18.
 */
abstract class AbstractRequest<T> {

    abstract val successCode: Int

    fun enqueue(call: Call<T>, listener: RequestCallbacks<T>) {
        call.enqueue(getCallback(listener))
    }

    fun execute(call: Call<T>): T? {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getCallback(listener: RequestCallbacks<T>): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.d(TAG, "Response code is: " + response.code())
                if (response.code() == successCode) {
                    val responseObject = response.body()

                    performExtraTask(responseObject)
                    Log.d(TAG, responseObject!!.toString())
                    listener.onSuccess(responseObject)

                    // some error handling
                } else {
                    try {
                        val jelement = JsonParser().parse(response.errorBody()!!.string())
                        val gson = Gson()
                        val err = gson.fromJson<SimpleError>(jelement, SimpleError::class.java!!)
                        listener.onFailure(err.error!!, response.code(), Throwable("Wrong response code, got " + response.code() + " expected was: " + successCode))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d(TAG, "Request failed")
                        listener.onFailure(null, response.code(), Throwable("Request Failed"))
                    }

                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e(TAG, "onFailure", t)
                listener.onFailure(null, 0, t)
            }
        }
    }

    // Let subclasses override the method to add behaviour
    fun performExtraTask(data: T?) {

    }

    companion object {
        private val TAG = AbstractRequest::class.java.simpleName
    }
}