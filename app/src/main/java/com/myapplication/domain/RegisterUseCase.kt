package com.myapplication.domain

import com.myapplication.data.remote.Repository
import com.myapplication.domain.base.Input
import com.myapplication.domain.base.InputType
import com.myapplication.domain.base.State
import com.myapplication.models.User
import com.myapplication.utility.ConnectionManager
import com.myapplication.utility.PreferenceManager
import com.myapplication.utility.logcat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: Repository,
    private val connectionManager: ConnectionManager,
    private val inputValidationManager: InputValidationManager,
    private val preferenceManager: PreferenceManager,
) {

    operator fun invoke(email: String?, password: String?, age: String?) = flow<State<User>> {
        val inputValidationState = inputValidationManager.validate<User>(
            Input(email, InputType.EMAIL, true),
            Input(password, InputType.PASSWORD, true),
            Input(age, InputType.AGE, true),
        )

        if (inputValidationState.isFailure) {
            emit(inputValidationState)

            return@flow
        }

        when {

            inputValidationState.isFailure -> emit(inputValidationState)

            connectionManager.isConnected.not() -> emit(State.Failure.InternetUnavailable())

            else -> {
                emit(State.loading())

                val response = repository.register(email!!, password!!, age!!)
                if (response.isSuccess) {
                    preferenceManager.authorizationToken = null

                    preferenceManager.authorizationToken = response.dataOrNull?.token
                }

                emit(response)
            }
        }
    }.catch { throwable ->
        logcat(throwable)

        emit(State.Failure.Generic())
    }
}