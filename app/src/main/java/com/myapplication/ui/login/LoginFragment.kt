package com.myapplication.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.myapplication.R
import com.myapplication.databinding.FragmentLoginBinding
import com.myapplication.domain.base.InputType
import com.myapplication.domain.base.InvalidInput
import com.myapplication.domain.base.State
import com.myapplication.ui.base.BaseFragment
import com.myapplication.ui.main.MainViewModel
import com.myapplication.utility.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val state = MutableLiveData<State<Any>>(State.initial())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            onClickListener = this@LoginFragment
            uiState = state.mapToDataBindingState()
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                register -> findNavController().navigateSafely(
                    R.id.action_fragment_registration, R.id.fragment_login
                )

                login -> login()
            }
        }
    }

    private fun login() {
        binding.apply {
            val email = email.text.toString()
            val password = password.text.toString()

            observe(viewModel.login(email, password)) {
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
                                if (inputError.inputType == InputType.EMAIL) {
                                    inputEmail.error = inputError.message
                                } else if (inputError.inputType == InputType.PASSWORD) {
                                    inputPassword.error = inputError.message
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