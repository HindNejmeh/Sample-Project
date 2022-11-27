package com.myapplication.ui.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.myapplication.R
import com.myapplication.databinding.FragmentRegistrationBinding
import com.myapplication.domain.base.InputType
import com.myapplication.domain.base.InvalidInput
import com.myapplication.domain.base.State
import com.myapplication.ui.base.BaseFragment
import com.myapplication.ui.main.MainViewModel
import com.myapplication.utility.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(R.layout.fragment_registration),
    View.OnClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: RegistrationViewModel by viewModels()

    private val state = MutableLiveData<State<Any>>(State.initial())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            onClickListener = this@RegistrationFragment
            uiState = state.mapToDataBindingState()
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                login -> findNavController().navigateUp()

                register -> register()
            }
        }
    }

    private fun register() {
        binding.apply {
            val email = email.text.toString()
            val password = password.text.toString()
            val age = age.text.toString()

            observe(viewModel.register(email, password, age)) {
                when {
                    it.isLoading -> {
                        hideKeyboard()
                        clearInputErrors()
                        setInputEnabled(false)

                        state.value = it
                    }

                    it.isFailure -> {
                        setInputEnabled(true)
                        state.value = State.initial()
                        if (it is InvalidInput) {
                            it.inputErrors.forEach { inputError ->
                                when (inputError.inputType) {
                                    InputType.EMAIL -> {
                                        inputEmail.error = inputError.message
                                    }

                                    InputType.PASSWORD -> {
                                        inputPassword.error = inputError.message
                                    }

                                    InputType.AGE -> {
                                        inputAge.error = inputError.message
                                    }
                                }
                            }
                        } else {
                            showStateMessage(it)
                        }
                    }

                    it.isSuccess -> {
                        mainViewModel.isUserLoggedIn = true
                        findNavController().navigate(R.id.to_main)
                    }
                }
            }
        }
    }
}