package com.myapplication.domain

import com.myapplication.R
import com.myapplication.domain.base.*
import android.content.Context
import android.util.Patterns
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InputValidationManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun <T> validate(vararg inputs: Input): State<T> {
        val inputErrors = inputs.mapNotNull {
            when (it.value) {
                is String? -> validateString(it)

                else -> null
            }
        }

        return if (inputErrors.isNotEmpty()) InvalidInput(inputErrors) else State.success()
    }

    private fun validateString(input: Input): InputError? {
        val string = input.value as? String
        if (string.isNullOrBlank()) {
            return if (input.isRequired) {
                // blank and required
                InputError(input.type, context.getString(R.string.message_field_required))
            } else {
                null
            }
        }

        val errorMessageId = when {
            input.type == InputType.EMAIL && !isEmailValid(string) -> R.string.message_email_invalid

            input.type == InputType.PASSWORD && !isPasswordValid(string) -> R.string.message_password_invalid

            input.type == InputType.AGE && !isAgeValid(string) -> R.string.message_age_invalid

            else -> null
        }

        return if (errorMessageId != null) InputError(
            input.type, context.getString(errorMessageId)
        ) else null
    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) = password.length in (6..12)

    private fun isAgeValid(age: String) = age.toInt() in (18..99)
}