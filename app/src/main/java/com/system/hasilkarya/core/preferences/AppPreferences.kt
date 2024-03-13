package com.system.hasilkarya.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val tokenKey = stringPreferencesKey("token")
    private val nameKey = stringPreferencesKey("name")
    private val userIdKey = stringPreferencesKey("user_id")

    suspend fun setToken(token: String) {
        dataStore.edit {
            it[tokenKey] = token
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[tokenKey]?:""
        }
    }

    suspend fun setName(name: String) {
        dataStore.edit {
            it[nameKey] = name
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map {
            it[nameKey]?:""
        }
    }

    suspend fun setUserId(userId: String) {
        dataStore.edit {
            it[this.userIdKey] = userId
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map {
            it[userIdKey]?:""
        }
    }
}