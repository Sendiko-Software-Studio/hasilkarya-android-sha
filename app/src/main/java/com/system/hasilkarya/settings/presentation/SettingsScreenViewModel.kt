package com.system.hasilkarya.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.ui.theme.AppTheme
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.settings.data.LogoutResponse
import com.system.hasilkarya.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenState())
    private val _name = repository.getName()
    private val _token = repository.getToken()
    private val _email = repository.getEmail()
    private val _theme = repository.getTheme()
    val state = combine(
        _name, _token, _email, _theme, _state
    ) { name, token, email, theme, state ->
        state.copy(name = name, token = token, email = email, theme = theme)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenState())

    private fun logout() {
        _state.update {
            it.copy(isLoading = true)
        }
        val token = "Bearer ${state.value.token}"
        val request = repository.logout(token)
        request.enqueue(
            object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    _state.update { it.copy(isLoading = false) }
                    when(response.code()) {
                        200 -> {
                            viewModelScope.launch {
                                repository.clearData()
                            }
                            _state.update {
                                it.copy(isPostSuccessful = true)
                            }
                        }

                        else -> _state.update {
                            it.copy(
                                isRequestFailed = FailedRequest(
                                    isFailed = true
                                ),
                                notificationMessage = "Mohon maaf, coba lagi dalam beberapa waktu"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    _state.update {
                        it.copy(
                            isRequestFailed = FailedRequest(
                                isFailed = true
                            ),
                            notificationMessage = "Mohon maaf, coba lagi dalam beberapa waktu"
                        )
                    }
                }

            }
        )
    }

    private fun changeTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.setTheme(theme)
        }
        _state.update {
            it.copy(theme = theme)
        }
    }

    fun onEvent(event: SettingsScreenEvent) {
        when(event) {
            SettingsScreenEvent.OnLogout -> logout()
            is SettingsScreenEvent.OnThemeChanged -> changeTheme(event.theme)
        }
    }

}