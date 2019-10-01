package com.ruitzei.foodie.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.drawee.backends.pipeline.Fresco
import com.ruitzei.foodie.service.ApiCalls
import com.ruitzei.foodie.service.RestClient


class FoodieApplication: Application() {
    var api: ApiCalls? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        Fresco.initialize(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        api = RestClient.createPublicApi()
    }

    fun getApiService(): ApiCalls? {
        return api
    }

    companion object {
        private val TAG = FoodieApplication::class.java.simpleName

        var instance: FoodieApplication? = null

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}