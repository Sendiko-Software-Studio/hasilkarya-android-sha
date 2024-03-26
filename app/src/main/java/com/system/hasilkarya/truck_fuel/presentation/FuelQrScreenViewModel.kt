package com.system.hasilkarya.truck_fuel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelRepository
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions
import com.system.hasilkarya.truck_fuel.data.TruckFuelRequest
import com.system.hasilkarya.truck_fuel.data.TruckFuelResponse
import com.system.hasilkarya.material.data.CheckDriverIdResponse
import com.system.hasilkarya.material.data.CheckStationIdResponse
import com.system.hasilkarya.material.data.CheckTruckIdResponse
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
class FuelQrScreenViewModel @Inject constructor(
    private val repository: TruckFuelRepository,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(FuelQrScreenState())
    private val _userId = repository.getUserId()
    private val _token = repository.getToken()
    val connectionStatus =
        combine(connectionObserver.observe(), _state) { connectionStatus, state ->
            state.copy(connectionStatus = connectionStatus)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FuelQrScreenState())

    val state = combine(_userId, _token, _state) { userId, token, state ->
        state.copy(token = token, userId = userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FuelQrScreenState())

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

    private fun postTruckFuel(fuelTruckEntity: FuelTruckEntity) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val data = TruckFuelRequest(
            truckId = fuelTruckEntity.truckId,
            driverId = fuelTruckEntity.driverId,
            stationId = fuelTruckEntity.stationId,
            volume = fuelTruckEntity.volume,
            odometer = fuelTruckEntity.odometer,
            gasOperatorId = fuelTruckEntity.userId,
            remarks = fuelTruckEntity.remarks
        )
        val request = repository.postFuels(token, data)
        if (connectionStatus.value.connectionStatus == Status.Available) {
            request.enqueue(
                object : Callback<TruckFuelResponse> {
                    override fun onResponse(
                        call: Call<TruckFuelResponse>,
                        response: Response<TruckFuelResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when (response.code()) {
                            201 -> _state.update {
                                it.copy(
                                    isPostSuccessful = true,
                                    notificationMessage = "Data tersimpan!"
                                )
                            }

                            else -> _state.update {
                                it.copy(
                                    isRequestFailed = FailedRequest(isFailed = true),
                                    notificationMessage = "Ups! Terjadi kesalahan."
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<TruckFuelResponse>, t: Throwable) {
                        viewModelScope.launch { repository.saveFuel(fuelTruckEntity) }
                        _state.update {
                            it.copy(
                                isPostSuccessful = true,
                                notificationMessage = "Data tersimpan!"
                            )
                        }
                    }

                }
            )
        } else {
            viewModelScope.launch { repository.saveFuel(fuelTruckEntity) }
            _state.update {
                it.copy(
                    isPostSuccessful = true,
                    notificationMessage = "Data tersimpan!"
                )
            }
        }
    }

    fun onEvent(event: TruckFuelQrScreenEvent) {
        when (event) {
            is TruckFuelQrScreenEvent.OnNavigateForm -> _state.update {
                it.copy(currentlyScanning = event.scanOptions)
            }

            is TruckFuelQrScreenEvent.OnTruckIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkTruckId(event.truckId)
                } else _state.update {
                    it.copy(truckId = event.truckId, currentlyScanning = ScanOptions.Driver)
                }
            }

            is TruckFuelQrScreenEvent.OnDriverIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkDriverId(event.driverId)
                } else _state.update {
                    it.copy(driverId = event.driverId, currentlyScanning = ScanOptions.Pos)
                }
            }

            is TruckFuelQrScreenEvent.OnStationIdRegistered -> {
                if (connectionStatus.value.connectionStatus == Status.Available) {
                    checkStationId(event.stationId)
                } else _state.update {
                    it.copy(stationId = event.stationId, currentlyScanning = ScanOptions.Volume)
                }
            }

            is TruckFuelQrScreenEvent.OnVolumeRegistered -> _state.update {
                it.copy(volume = event.volume, currentlyScanning = ScanOptions.None)
            }

            is TruckFuelQrScreenEvent.OnOdometerChange -> _state.update {
                it.copy(odometer = event.odometer)
            }

            TruckFuelQrScreenEvent.OnClearOdometer -> _state.update {
                it.copy(odometer = "")
            }

            is TruckFuelQrScreenEvent.OnRemarksChange -> _state.update {
                it.copy(remarks = event.remarks)
            }

            TruckFuelQrScreenEvent.OnClearRemarks -> _state.update {
                it.copy(remarks = "")
            }

            TruckFuelQrScreenEvent.SaveTruckFuelTransaction -> {
                val data = FuelTruckEntity(
                    truckId = state.value.truckId,
                    driverId = state.value.driverId,
                    stationId = state.value.stationId,
                    volume = state.value.volume,
                    userId = state.value.userId,
                    odometer = state.value.odometer.toDouble(),
                    remarks = state.value.remarks
                )
                postTruckFuel(data)
            }

            TruckFuelQrScreenEvent.NotificationClear -> _state.update {
                it.copy(notificationMessage = "")
            }
        }
    }
}