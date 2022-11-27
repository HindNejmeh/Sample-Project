package com.myapplication.utility

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.myapplication.domain.base.State

enum class DataBindingState {

    INITIAL, LOADING, DATA, SUCCESS, FAILURE;

    companion object {

        fun from(state: State<*>) = when (state) {
            is State.Initial -> INITIAL
            is State.Loading -> LOADING
            is State.Success.Data -> DATA
            is State.Success -> SUCCESS
            is State.Failure -> FAILURE
        }
    }

    val isInitial get() = this == INITIAL

    val isLoading get() = this == LOADING

    val isData get() = this == DATA

    val isSuccess get() = this == DATA || this == SUCCESS

    val isFailure get() = this == FAILURE
}

fun <T> LiveData<State<T>>.mapToDataBindingState() = map { DataBindingState.from(it) }