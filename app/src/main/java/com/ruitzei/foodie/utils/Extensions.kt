package com.ruitzei.foodie.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
//import com.ruitzei.foodie.model.Point

fun Activity.closeKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

//fun LatLng.toPoint(): Point {
//    return Point(coordinates = listOf(this.latitude, this.longitude))
//}