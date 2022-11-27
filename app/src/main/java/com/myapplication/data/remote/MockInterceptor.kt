package com.myapplication.data.remote

import android.content.Context
import com.myapplication.di.BaseUrl
import com.myapplication.utility.ConnectionManager
import com.myapplication.utility.readJsonAsset
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    @BaseUrl private val baseUrl: String,
) : Interceptor {

    @Inject
    lateinit var connectionManager: ConnectionManager

    override fun intercept(chain: Interceptor.Chain) = Response.Builder().run {
        val request = chain.request()

        request(request)

        protocol(Protocol.HTTP_2)

        val responseAssetName = getResponseAssetName(request)

        if (responseAssetName != "Home") {

            if (!connectionManager.isConnected) {
                code(400)
                message("No Connection")

                val responseJson = readJsonAsset(context, "Failure")
                val responseBody =
                    responseJson.toResponseBody("application/json".toMediaTypeOrNull())

                body(responseBody)
                addHeader("Content-Type", "application/json")
            } else {
                val responseJson = readJsonAsset(context, responseAssetName)
                val responseBody =
                    responseJson.toResponseBody("application/json".toMediaTypeOrNull())

                body(responseBody)
                addHeader("Content-Type", "application/json")
                code(HttpURLConnection.HTTP_OK)
                message("OK")
            }

            runBlocking { delay(1000) }

            build()
        } else {
            chain.proceed(request)
        }
    }

    private fun getResponseAssetName(request: Request): String {
        val url = request.url
        val endpoint = url.toString().substringAfter(baseUrl).substringBefore("?")

        return when (endpoint) {
            "login" -> "LogIn"

            "register" -> "Register"

            else -> "Home"
        }
    }
}