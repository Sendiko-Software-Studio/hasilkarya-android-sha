package com.system.hasilkarya.profile.presentation

import com.system.hasilkarya.core.ui.theme.AppTheme


sealed class ProfileScreenEvent {
    data object OnLogout: ProfileScreenEvent()
    data class OnThemeChanged(val theme: AppTheme): ProfileScreenEvent()
}
