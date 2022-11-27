package com.myapplication.ui.login

import com.myapplication.domain.LogInUseCase
import com.myapplication.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val logInUseCase: LogInUseCase) : BaseViewModel() {

    fun login(email: String?, password: String?) =
        logInUseCase(email, password).asLiveDataInViewModelScope()
}