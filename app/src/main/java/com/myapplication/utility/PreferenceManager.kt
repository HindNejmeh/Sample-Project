package com.myapplication.utility

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val KEY_AUTHORIZATION_TOKEN = "authorizationToken"
    }

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    var authorizationToken
        get() = preferences.getString(KEY_AUTHORIZATION_TOKEN, null)
        set(value) = preferences.edit { putString(KEY_AUTHORIZATION_TOKEN, value) }

}