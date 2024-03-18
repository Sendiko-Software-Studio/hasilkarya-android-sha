package com.system.hasilkarya.profile.presentation

sealed class ProfileScreenEvent {
    data object OnLogout: ProfileScreenEvent()
}
