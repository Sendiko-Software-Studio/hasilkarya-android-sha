package com.system.hasilkarya.station.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.StationEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.repositories.station.StationRepository
import com.system.hasilkarya.station.data.GetStationResponse
import com.system.hasilkarya.station.presentation.component.StationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
class StationQrScreenViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    preferences: AppPreferences,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _connectionStatus = connectionObserver.observe()
    private val _state = MutableStateFlow(StationQrScreenState())
    private val _token = preferences.getToken()
    val state = combine(_connectionStatus, _token, _state) { connectionStatus, token, state ->
        state.copy(connectionStatus = connectionStatus, token = token)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StationQrScreenState())

    private fun isStationValid(token: String, stationId: String): Int {
        val request = if (state.value.scanningFor == StationType.MINE) {
            stationRepository.checkMineStationId(id = stationId, token = token)
        } else {
            stationRepository.checkGasStationId(id = stationId, token = token)
        }
        return request.execute().code()
    }

    private fun getStation(stationId: String, connectionStatus: Status) {
        _state.update { it.copy(isLoading = true) }
        Log.i("CONNECTION_STATUS", connectionStatus.name)
        val token = "Bearer ${state.value.token}"
        if (state.value.connectionStatus == Status.Available) {
            viewModelScope.launch(Dispatchers.IO) {
                when (isStationValid(token = token, stationId = stationId)) {
                    200 -> {
                        val request = stationRepository.getStationFromApi(stationId, token)
                        request.enqueue(
                            object : Callback<GetStationResponse> {
                                override fun onResponse(
                                    call: Call<GetStationResponse>,
                                    response: Response<GetStationResponse>
                                ) {
                                    _state.update { it.copy(isLoading = false) }
                                    when (response.code()) {
                                        200 -> {
                                            Log.i("RESPONSE_BODY", "${response.body()?.data}")
                                            val station = StationEntity(
                                                id = 1,
                                                name = response.body()?.data?.name ?: "",
                                                regency = response.body()?.data?.regency ?: "",
                                                province = response.body()?.data?.province ?: "",
                                                stationId = response.body()?.data?.id ?: "",
                                            )
                                            viewModelScope.launch {
                                                stationRepository.saveStation(station)
                                                _state.update {
                                                    it.copy(
                                                        notificationMessage = "Pos disimpan.",
                                                        stationId = stationId
                                                    )
                                                }
                                                delay(1000)
                                                _state.update {
                                                    it.copy(
                                                        isRequestSuccess = true,
                                                    )
                                                }
                                            }
                                            clearState()
                                        }

                                        else -> {
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    isRequestFailed = true,
                                                    notificationMessage = "Server error."
                                                )
                                            }
                                            clearState()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<GetStationResponse>, t: Throwable) {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            isRequestFailed = true,
                                            notificationMessage = "Server error."
                                        )
                                    }
                                    clearState()
                                }

                            }
                        )
                    }

                    404 -> viewModelScope.launch {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRequestFailed = true,
                                notificationMessage = "Pos tidak ditemukan."
                            )
                        }
                        delay(1000)
                        clearState()
                    }
                }
            }
        } else {
            val data = StationEntity(
                id = 1,
                name = "Station berhasil disimpan.",
                regency = "",
                province = "",
                stationId = stationId,
            )
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = false,
                        notificationMessage = "Pos disimpan.",
                        stationId = stationId
                    )
                }
                stationRepository.saveStation(data)
                delay(1000)
                _state.update {
                    it.copy(
                        isRequestSuccess = true,
                    )
                }
            }
            clearState()
        }
    }

    private fun clearState() {
        viewModelScope.launch {
            delay(1000)
            _state.update {
                it.copy(
                    notificationMessage = "",
                    isRequestFailed = false,
                    isRequestSuccess = false,
                    isLoading = false,
                )
            }
        }
    }

    fun onEvent(event: StationQrScreenEvent) {
        when (event) {
            is StationQrScreenEvent.OnQrCodeScanned -> {
                getStation(event.qrCode, event.connectionStatus)
            }
        }
    }

}