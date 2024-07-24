package com.system.shailendra.settings.presentation

import com.system.shailendra.core.ui.theme.AppTheme
import com.system.shailendra.core.ui.utils.FailedRequest

data class SettingsScreenState(
    val name: String = "",
    val email: String = "",
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isRequestFailed: FailedRequest = FailedRequest(),
    val notificationMessage: String = "",
    val token: String = "",
    val rapidMode: Boolean = false,
    val showingThemeOptions: Boolean = false,
    val theme: AppTheme = AppTheme.Default
)
