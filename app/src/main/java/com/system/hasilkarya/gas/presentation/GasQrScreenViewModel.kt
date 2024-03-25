package com.system.hasilkarya.gas.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.GasEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.repositories.GasRepository
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.presentation.ScanOptions
import com.system.hasilkarya.gas.data.TruckGasRequest
import com.system.hasilkarya.gas.data.TruckGasResponse
import com.system.hasilkarya.material.data.CheckDriverIdResponse
import com.system.hasilkarya.material.data.CheckStationIdResponse
import com.system.hasilkarya.material.data.CheckTruckIdResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GasQrScreenViewModel @Inject constructor(
    private val repository: GasRepository,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(GasQrScreenState())
    private val _userId = repository.getUserId()
    private val _token = repository.getToken()
    val connectionStatus =
        combine(connectionObserver.observe(), _state) { connectionStatus, state ->
            state.copy(connectionStatus = connectionStatus)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GasQrScreenState())

    val state = combine(_userId, _token, _state) { userId, token, state ->
        state.copy(token = token, userId = userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GasQrScreenState())

    private fun checkDriverId(driverId: String) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val request = repository.checkDriverId(token, driverId)
        request.enqueue(
            object : Callback<CheckDriverIdResponse> {
                override fun onResponse(
                    call: Call<CheckDriverIdResponse>,
                    response: Response<CheckDriverIdResponse>
                ) {
                    _state.update { it.copy(isLoading = false) }
                    when (response.code()) {
                        200 -> _state.update {
                            it.copy(driverId = driverId, currentlyScanning = ScanOptions.Pos)
                        }

                        else -> _state.update {
                            it.copy(
                                notificationMessage = "Qr invalid, mohon scan ulang",
                                isRequestFailed = FailedRequest(isFailed = true)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<CheckDriverIdResponse>, t: Throwable) {
                    _state.update {
                        it.copy(
                            notificationMessage = "Qr invalid, mohon scan ulang",
                            isRequestFailed = FailedRequest(isFailed = true)
                        )
                    }
                }

            }
        )
    }

    private fun checkTruckId(truckId: String) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val request = repository.checkTruckId(token, truckId)
        request.enqueue(
            object : Callback<CheckTruckIdResponse> {
                override fun onResponse(
                    call: Call<CheckTruckIdResponse>,
                    response: Response<CheckTruckIdResponse>
                ) {
                    _state.update { it.copy(isLoading = false) }
                    when (response.code()) {
                        200 -> _state.update {
                            it.copy(truckId = truckId, currentlyScanning = ScanOptions.Driver)
                        }

                        else -> _state.update {
                            it.copy(
                                notificationMessage = "Qr invalid, mohon scan ulang",
                                isRequestFailed = FailedRequest(isFailed = true)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<CheckTruckIdResponse>, t: Throwable) {
                    _state.update {
                        it.copy(
                            notificationMessage = "Qr invalid, mohon scan ulang",
                            isRequestFailed = FailedRequest(isFailed = true)
                        )
                    }
                }

            }
        )
    }

    private fun checkStationId(stationId: String) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val request = repository.checkStationId(token, stationId)
        request.enqueue(
            object : Callback<CheckStationIdResponse> {
                override fun onResponse(
                    call: Call<CheckStationIdResponse>,
                    response: Response<CheckStationIdResponse>
                ) {
                    _state.update { it.copy(isLoading = false) }
                    when (response.code()) {
                        200 -> _state.update {
                            it.copy(stationId = stationId, currentlyScanning = ScanOptions.Volume)
                        }

                        else -> _state.update {
                            it.copy(
                                notificationMessage = "Qr invalid, mohon scan ulang",
                                isRequestFailed = FailedRequest(isFailed = true)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<CheckStationIdResponse>, t: Throwable) {
                    _state.update {
                        it.copy(
                            notificationMessage = "Qr invalid, mohon scan ulang",
                            isRequestFailed = FailedRequest(isFailed = true)
                        )
                    }
                }

            }
        )
    }

    private fun postTruckGas(gasEntity: GasEntity) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val data = TruckGasRequest(
            truckId = gasEntity.truckId,
            driverId = gasEntity.driverId,
            stationId = gasEntity.stationId,
            volume = gasEntity.volume,
            odometer = gasEntity.odometer,
            gasOperatorId = gasEntity.userId
        )
        val request = repository.postGas(token, data)
        if (connectionStatus.value.connectionStatus == Status.Available) {
            request.enqueue(
                object : Callback<TruckGasResponse> {
                    override fun onResponse(
                        call: Call<TruckGasResponse>,
                        response: Response<TruckGasResponse>
                    ) {
                        Log.i("TEST", "postTruckGas: active")
                        _state.update { it.copy(isLoading = false) }
                        when(response.code()) {
                            201 -> _state.update { it.copy(isPostSuccessful = true, notificationMessage = "Data tersimpan!") }
                            else -> _state.update { it.copy(isRequestFailed = FailedRequest(isFailed = true), notificationMessage = "ups") }
                        }
                    }

                    override fun onFailure(call: Call<TruckGasResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                }
            )
        }
    }

    fun onEvent(event: GasQrScreenEvent) {
        when (event) {
            is GasQrScreenEvent.OnNavigateForm -> _state.update {
                it.copy(currentlyScanning = event.scanOptions)
            }

            is GasQrScreenEvent.OnTruckIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkTruckId(event.truckId)
                } else _state.update {
                    it.copy(truckId = event.truckId, currentlyScanning = ScanOptions.Driver)
                }
            }

            is GasQrScreenEvent.OnDriverIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkDriverId(event.driverId)
                } else _state.update {
                    it.copy(driverId = event.driverId, currentlyScanning = ScanOptions.Pos)
                }
            }

            is GasQrScreenEvent.OnStationIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkStationId(event.stationId)
                } else _state.update {
                    it.copy(stationId = event.stationId, currentlyScanning = ScanOptions.Volume)
                }
            }

            is GasQrScreenEvent.OnVolumeRegistered -> _state.update {
                it.copy(volume = event.volume, currentlyScanning = ScanOptions.None)
            }

            is GasQrScreenEvent.OnOdometerChange -> _state.update {
                it.copy(odometer = event.odometer)
            }

            GasQrScreenEvent.OnClearOdometer -> _state.update {
                it.copy(odometer = "")
            }

            is GasQrScreenEvent.OnRemarksChange -> _state.update {
                it.copy(remarks = event.remarks)
            }

            GasQrScreenEvent.OnClearRemarks -> _state.update {
                it.copy(remarks = "")
            }

            GasQrScreenEvent.SaveGasTransaction -> {
                val data = GasEntity(
                    truckId = state.value.truckId,
                    driverId = state.value.driverId,
                    stationId = state.value.stationId,
                    volume = state.value.volume,
                    userId = state.value.userId,
                    odometer = state.value.odometer.toDouble()
                )
                postTruckGas(data)
            }

            GasQrScreenEvent.NotificationClear -> _state.update {
                it.copy(notificationMessage = "")
            }
        }
    }
}