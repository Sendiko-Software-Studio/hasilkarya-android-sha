package com.system.shailendra.settings.presentation

import com.system.shailendra.core.ui.theme.AppTheme


sealed class SettingsScreenEvent {
    data object OnLogout: SettingsScreenEvent()
    data class OnThemeChanged(val theme: AppTheme): SettingsScreenEvent()
    data class OnRapidModeChanged(val rapidMode: Boolean): SettingsScreenEvent()
    data class OnShowThemeOptionsChanged(val isShowing: Boolean): SettingsScreenEvent()
}
