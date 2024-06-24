package com.system.hasilkarya.settings.domain

import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.ui.theme.AppTheme
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val preferences: AppPreferences,
    private val apiServices: ApiServices
) {

    fun getName() = preferences.getName()
    fun getToken() = preferences.getToken()
    fun getEmail() = preferences.getEmail()
    fun getTheme() = preferences.getTheme()
    suspend fun setTheme(theme: AppTheme) = preferences.setTheme(theme)
    suspend fun clearData() = preferences.clearData()
    fun logout(token: String) = apiServices.logout(token)
}