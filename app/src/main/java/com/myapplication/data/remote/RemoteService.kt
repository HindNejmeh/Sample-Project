package com.myapplication.data.remote

import com.myapplication.data.base.RemoteResponse
import com.myapplication.data.remote.requests.LoginBody
import com.myapplication.data.remote.requests.RegisterBody
import com.myapplication.models.Home
import com.myapplication.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val DEFAULT_BASE_URL = "https://pixabay.com/"

const val PIXABAY_API_KEY = "31628510-3af70eb3f6fec93f429b60e8d"

interface RemoteService {

    @POST("login")
    suspend fun login(@Body body: LoginBody): RemoteResponse<User>

    @POST("register")
    suspend fun register(@Body body: RegisterBody): RemoteResponse<User>

    @GET("api")
    suspend fun getHome(
        @Query("page") pageNumber: Int,
        @Query("per_page") pageSize: String = "10",
        @Query("key") key: String = PIXABAY_API_KEY
    ): RemoteResponse<Home>
}