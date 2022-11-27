package com.myapplication.data.base

import com.myapplication.domain.base.State
import com.myapplication.utility.logcat
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CancellationException
import retrofit2.Response

typealias RemoteResponse<T> = Response<T>

abstract class BaseRepository(protected val moshi: Moshi) {

    protected suspend fun <T> sendRemoteRequest(request: suspend () -> RemoteResponse<T>): State<T> {
        var state: State<T>

        try {
            val response = request()

            state = when (response.code()) {
                in 200..299 -> {
                    val baseBody = response.body()

                    State.success(baseBody)
                }

                else -> {
                    logcat("${response.errorBody()}")
                    State.failure()
                }
            }
        } catch (throwable: Throwable) {
            logcat(throwable)

            if (throwable is CancellationException) throw throwable

            state = State.failure()
        }

        return state
    }

}