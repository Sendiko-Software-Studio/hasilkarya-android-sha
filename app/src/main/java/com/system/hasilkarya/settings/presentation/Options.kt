package com.system.hasilkarya.settings.presentation

import com.system.hasilkarya.core.ui.theme.AppTheme


data class Options(
    val theme: AppTheme = AppTheme.Default,
    val rapidMode: Boolean = false,
)
