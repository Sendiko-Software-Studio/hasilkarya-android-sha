package com.system.shailendra.settings.presentation

import com.system.shailendra.core.ui.theme.AppTheme


data class Options(
    val theme: AppTheme = AppTheme.Default,
    val rapidMode: Boolean = false,
)
