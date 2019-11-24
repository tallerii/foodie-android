package com.ruitzei.foodie.utils

import android.preference.PreferenceManager
import com.ruitzei.foodie.application.FoodieApplication

object PreferenceManager {
    val TOKEN: String = "TOKEN"

    fun saveToken(token: String) {
        PreferenceManager.getDefaultSharedPreferences(FoodieApplication.instance!!.applicationContext).edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String {
        return PreferenceManager.getDefaultSharedPreferences(FoodieApplication.instance!!.applicationContext).getString(TOKEN, "").orEmpty()
    }

    fun removeToken() {
        saveToken("")
    }
}