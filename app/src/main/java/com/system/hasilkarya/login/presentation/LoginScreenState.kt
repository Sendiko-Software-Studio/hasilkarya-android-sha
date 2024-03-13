package com.system.hasilkarya.login.presentation

import com.system.hasilkarya.core.ui.utils.ErrorTextField
import com.system.hasilkarya.core.ui.utils.FailedRequest

data class LoginScreenState(
    val emailText: String = "",
    val emailErrorState: ErrorTextField = ErrorTextField(),
    val passwordText: String = "",
    val passwordErrorState: ErrorTextField = ErrorTextField(),
    val isPasswordVisible: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val notificationMessage: String = "",
    val isRequestFailed: FailedRequest = FailedRequest()
)
