package com.system.hasilkarya.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginScreenViewModel: ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when(event) {
            is LoginScreenEvent.OnEmailChange -> _state.update {
                it.copy(emailText = event.email)
            }
            LoginScreenEvent.OnEmailClear -> _state.update {
                it.copy(emailText = "")
            }
            is LoginScreenEvent.OnPasswordChange -> _state.update {
                it.copy(passwordText = event.password)
            }
            LoginScreenEvent.OnPasswordClear -> _state.update {
                it.copy(passwordText = "")
            }
            is LoginScreenEvent.OnPasswordVisibilityChange -> _state.update {
                it.copy(isPasswordVisible = event.isVisible)
            }
            LoginScreenEvent.OnLoginClick -> TODO()
        }
    }
}