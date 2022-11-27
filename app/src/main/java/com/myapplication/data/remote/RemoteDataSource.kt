package com.myapplication.data.remote

import androidx.databinding.ktx.BuildConfig
import com.myapplication.di.BaseUrl
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    @BaseUrl baseUrl: String,
    moshi: Moshi,
    headerInterceptor: HeaderInterceptor,
    mockInterceptor: MockInterceptor? = null,
) {

    val remoteService: RemoteService

    init {
        val okHttpClient = OkHttpClient.Builder().run {
            addInterceptor(headerInterceptor)

            if (mockInterceptor != null) addInterceptor(mockInterceptor)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                addInterceptor(loggingInterceptor)
            }

            build()
        }

        val retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

        remoteService = retrofit.create()
    }
}