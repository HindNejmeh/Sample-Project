package com.myapplication.ui.registration

import com.myapplication.domain.RegisterUseCase
import com.myapplication.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    BaseViewModel() {

    fun register(email: String?, password: String?, age: String?) =
        registerUseCase(email, password, age).asLiveDataInViewModelScope()
}