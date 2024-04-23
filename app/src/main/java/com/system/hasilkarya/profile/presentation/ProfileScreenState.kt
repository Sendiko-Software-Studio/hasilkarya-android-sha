package com.system.hasilkarya.profile.presentation

import com.system.hasilkarya.core.ui.theme.AppTheme
import com.system.hasilkarya.core.ui.utils.FailedRequest

data class ProfileScreenState(
    val name: String = "",
    val email: String = "",
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isRequestFailed: FailedRequest = FailedRequest(),
    val notificationMessage: String = "",
    val token: String = "",
    val theme: AppTheme = AppTheme.Default
)
