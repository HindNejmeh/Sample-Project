package com.myapplication.data.remote

import com.myapplication.utility.PreferenceManager
import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderInterceptor @Inject constructor(private val preferenceManager: PreferenceManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.request().newBuilder().run {
        addHeader("Accept", "application/json")

        preferenceManager.authorizationToken?.let { addHeader("Authorization", "Bearer $it") }

        chain.proceed(build())
    }
}