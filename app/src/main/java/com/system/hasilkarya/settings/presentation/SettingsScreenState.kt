package com.system.hasilkarya.settings.presentation

import com.system.hasilkarya.core.ui.theme.AppTheme
import com.system.hasilkarya.core.ui.utils.FailedRequest

data class SettingsScreenState(
    val name: String = "",
    val email: String = "",
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isRequestFailed: FailedRequest = FailedRequest(),
    val notificationMessage: String = "",
    val token: String = "",
    val theme: AppTheme = AppTheme.Default
)
