package com.system.shailendra.login.presentation

import com.system.shailendra.core.ui.utils.ErrorTextField
import com.system.shailendra.core.ui.utils.FailedRequest

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
