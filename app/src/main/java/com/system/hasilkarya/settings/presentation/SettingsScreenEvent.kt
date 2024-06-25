package com.system.hasilkarya.settings.presentation

import com.system.hasilkarya.core.ui.theme.AppTheme


sealed class SettingsScreenEvent {
    data object OnLogout: SettingsScreenEvent()
    data class OnThemeChanged(val theme: AppTheme): SettingsScreenEvent()
    data class OnRapidModeChanged(val rapidMode: Boolean): SettingsScreenEvent()
    data class OnShowThemeOptionsChanged(val isShowing: Boolean): SettingsScreenEvent()
}
