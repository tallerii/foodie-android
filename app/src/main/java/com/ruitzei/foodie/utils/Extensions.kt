package com.ruitzei.foodie.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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


val SPANISH_LOCALE = Locale("es", "ES")

fun Double.toStringWithTwoDecimals(locale: Locale? = SPANISH_LOCALE): String {
    val bd = BigDecimal(this)

    return formatNumber(
        number = bd,
        decimalSeparator = ',',
        thousandsSeparator = '.',
        minimumDecimalPlaces = 2,
        maximumDecimalPlaces = 2,
        separator = "",
        currencySymbol = ""
    )
}

private fun formatNumber(number: BigDecimal, decimalSeparator: Char, thousandsSeparator: Char, minimumDecimalPlaces: Int, maximumDecimalPlaces: Int, currencySymbol: String, separator: String): String {

    // Set formatters
    val dfs = DecimalFormatSymbols()
    dfs.decimalSeparator = decimalSeparator
    dfs.groupingSeparator = thousandsSeparator
    val df = DecimalFormat()
    df.decimalFormatSymbols = dfs
    df.minimumFractionDigits = minimumDecimalPlaces
    df.maximumFractionDigits = maximumDecimalPlaces
    df.roundingMode = RoundingMode.HALF_UP

    // return formatted string
    return if (currencySymbol.isNotEmpty()) {
        currencySymbol + separator + df.format(number)
    } else {
        df.format(number)
    }
}