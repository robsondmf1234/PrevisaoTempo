package com.example.previsodotempo.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}


fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun converter(tempDouble: Double?) = tempDouble?.roundToInt()

fun formaterTempToString(tempInt: Int?) = tempInt.toString() + "° C"
fun formaterMaxTempToString(tempInt: Int?) = "Max: " + tempInt.toString() + "º C"
fun formaterMinTempToString(tempInt: Int?) = "Min: " + tempInt.toString() + "º C"

fun formaterToStringPercenatge(tempInt: Int?) = tempInt.toString() + "%"