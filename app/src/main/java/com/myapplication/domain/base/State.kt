package com.myapplication.domain.base

import com.myapplication.R

sealed class State<out T> : Consumable() {

    abstract val message: String?

    companion object {

        fun <T> initial(message: String? = null) = Initial<T>(message)

        fun <T> loading(message: String? = null) = Loading.Generic<T>(message)

        fun <T> success(data: T? = null, message: String? = null) =
            if (data != null) Success.Data<T>(data, message) else Success.Generic(message)

        fun <T> failure(message: String? = null) = Failure.Generic<T>(message)
    }

    data class Initial<T>(override val message: String? = null) : State<T>()

    sealed class Loading<T> : State<T>() {

        data class Generic<T>(override val message: String? = null) : Loading<T>()
    }

    sealed class Success<T> : State<T>() {

        data class Generic<T>(override val message: String? = null) : Success<T>()

        data class Data<T>(val data: T, override val message: String? = null) : Success<T>()

    }

    sealed class Failure<T> : State<T>() {

        data class Generic<T>(override val message: String? = null) : Failure<T>()

        data class InternetUnavailable<T>(override val message: String? = null) : Failure<T>()
    }

    val dataOrNull get() = if (this is Success.Data) data else null

    val isInitial get() = this is Initial

    val isLoading get() = this is Loading

    val isSuccess get() = this is Success

    val isFailure get() = this is Failure
}

val State<*>.fallbackMessage
    get() = when (this) {
        is State.Success -> R.string.message_success

        is State.Failure.InternetUnavailable -> R.string.message_internet_unavailable

        is State.Failure -> R.string.message_failure

        else -> null
    }