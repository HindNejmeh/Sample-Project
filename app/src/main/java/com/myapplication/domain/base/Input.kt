package com.myapplication.domain.base

enum class InputType {
    EMAIL, PASSWORD, AGE
}

data class Input(
    val value: Any?,
    val type: InputType,
    val isRequired: Boolean,
)

data class InputError(val inputType: InputType, val message: String)

data class InvalidInput<T>(
    val inputErrors: List<InputError>, override val message: String? = null
) : State.Failure<T>()