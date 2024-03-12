package com.system.hasilkarya.login.presentation

import com.system.hasilkarya.core.ui.utils.ErrorTextField

data class LoginScreenState(
    val emailText: String = "",
    val emailErrorState: ErrorTextField = ErrorTextField(),
    val passwordText: String = "",
    val passwordErrorState: ErrorTextField = ErrorTextField(),
    val isPasswordVisible: Boolean = false,
)
