package com.system.shailendra.settings.domain

import com.system.shailendra.core.network.ApiServices
import com.system.shailendra.core.preferences.AppPreferences
import com.system.shailendra.core.ui.theme.AppTheme
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val preferences: AppPreferences,
    private val apiServices: ApiServices
) {

    fun getName() = preferences.getName()
    fun getToken() = preferences.getToken()
    fun getEmail() = preferences.getEmail()
    fun getTheme() = preferences.getTheme()
    fun getRapidMode() = preferences.getRapidMode()
    suspend fun setRapidMode(rapidMode: Boolean) = preferences.setRapidMode(rapidMode)
    suspend fun setTheme(theme: AppTheme) = preferences.setTheme(theme)
    suspend fun clearData() = preferences.clearData()
    fun logout(token: String) = apiServices.logout(token)
}