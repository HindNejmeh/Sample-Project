package com.myapplication.data.remote

import com.myapplication.data.base.BasePagingSource
import com.myapplication.data.remote.requests.LoginBody
import com.myapplication.data.remote.requests.RegisterBody
import com.squareup.moshi.Moshi
import javax.inject.Inject

class Repository @Inject constructor(
    moshi: Moshi,
    private val remoteService: RemoteService,
) : com.myapplication.data.base.BaseRepository(moshi) {

    suspend fun register(email: String, password: String, age: String) = sendRemoteRequest {
        val body = RegisterBody(email, password, age)
        remoteService.register(body)
    }

    suspend fun login(email: String, password: String) = sendRemoteRequest {
        val body = LoginBody(email, password)
        remoteService.login(body)
    }

    private suspend fun getHome(pageNumber: Int) =
        sendRemoteRequest { remoteService.getHome(pageNumber) }

    fun getPagedHome() =
        BasePagingSource(pageRequest = ::getHome, responsePageGetter = { it.images })
}