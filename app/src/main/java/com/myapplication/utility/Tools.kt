package com.myapplication.utility

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

private const val LOG_TAG = "MyApplication"

val screenWidth get() = Resources.getSystem().displayMetrics.widthPixels

fun logcat(message: String?) {
    Log.d(LOG_TAG, message ?: "null")
}

fun logcat(throwable: Throwable, message: String = "") {
    Log.e(LOG_TAG, message, throwable)
}

fun showToast(context: Context, @StringRes stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, stringId, duration).show()
}

fun showToast(context: Context, message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message ?: "null", duration).show()
}

fun readJsonAsset(context: Context, assetName: String) =
    context.assets.open("$assetName.json").bufferedReader().use { it.readText() }
