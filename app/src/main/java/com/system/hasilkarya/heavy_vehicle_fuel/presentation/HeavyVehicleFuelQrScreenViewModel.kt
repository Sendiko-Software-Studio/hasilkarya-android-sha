package com.system.hasilkarya.heavy_vehicle_fuel.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleFuelRepository
import com.system.hasilkarya.core.ui.utils.ErrorTextField
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.CheckDriverIdResponse
import com.system.hasilkarya.dashboard.data.CheckStationIdResponse
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions
import com.system.hasilkarya.heavy_vehicle_fuel.data.CheckHeavyVehicleIdResponse
import com.system.hasilkarya.heavy_vehicle_fuel.data.HeavyVehicleFuelRequest
import com.system.hasilkarya.heavy_vehicle_fuel.data.HeavyVehicleFuelResponse
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HeavyVehicleFuelQrScreenViewModel @Inject constructor(
    private val repository: HeavyVehicleFuelRepository,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(HeavyVehicleFuelQrScreenState())
    private val _userId = repository.getUserId()
    private val _token = repository.getToken()
    val connectionStatus =
        combine(connectionObserver.observe(), _state) { connectionStatus, state ->
            state.copy(connectionStatus = connectionStatus)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HeavyVehicleFuelQrScreenState()
        )
    val state = combine(_userId, _token, _state) { userId, token, state ->
        state.copy(
            userId = userId,
            token = token
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HeavyVehicleFuelQrScreenState())

    private fun checkHeavyVehicleId(heavyVehicleId: String) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val request = repository.checkHeavyVehicleId(token, heavyVehicleId)
        request.enqueue(
            object : Callback<CheckHeavyVehicleIdResponse> {
                override fun onResponse(
                    call: Call<CheckHeavyVehicleIdResponse>,
                    response: Response<CheckHeavyVehicleIdResponse>
                ) {
                    Log.i("DEBUG", "onResponse: active")
                    _state.update { it.copy(isLoading = false) }
                    when (response.code()) {
                        200 -> _state.update {
                            it.copy(
                                heavyVehicleId = heavyVehicleId,
                                currentlyScanning = ScanOptions.Driver
                            )
                        }

                        else -> _state.update {
                            it.copy(
                                notificationMessage = "Qr invalid, mohon scan ulang",
                                isRequestFailed = FailedRequest(isFailed = true)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<CheckHeavyVehicleIdResponse>, t: Throwable) {
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

    private fun postHeavyVehicleFuel(
        heavyVehicleEntity: FuelHeavyVehicleEntity,
        connectionStatus: Status
    ) {
        val token = "Bearer ${state.value.token}"
        val data = HeavyVehicleFuelRequest(
            heavyVehicleId = heavyVehicleEntity.heavyVehicleId,
            driverId = heavyVehicleEntity.driverId,
            stationId = heavyVehicleEntity.stationId,
            gasOperatorId = heavyVehicleEntity.gasOperatorId,
            volume = heavyVehicleEntity.volume,
            hourmeter = heavyVehicleEntity.hourmeter,
            remarks = heavyVehicleEntity.remarks,
            date = heavyVehicleEntity.date
        )
        if (connectionStatus == Status.Available) {
            _state.update { it.copy(isLoading = true) }
            val request = repository.postHeavyVehicleFuel(token, data)
            request.enqueue(
                object : Callback<HeavyVehicleFuelResponse> {
                    override fun onResponse(
                        call: Call<HeavyVehicleFuelResponse>,
                        response: Response<HeavyVehicleFuelResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when (response.code()) {
                            201 -> _state.update {
                                it.copy(
                                    isPostSuccessful = true,
                                    notificationMessage = "Data berhasil disimpan!"
                                )
                            }

                            else -> viewModelScope.launch {
                                repository.storeHeavyVehicleFuel(heavyVehicleEntity)
                                _state.update { it.copy(notificationMessage = "Data berhasil disimpan!") }
                            }
                        }
                    }

                    override fun onFailure(call: Call<HeavyVehicleFuelResponse>, t: Throwable) {
                        viewModelScope.launch {
                            repository.storeHeavyVehicleFuel(heavyVehicleEntity)
                            _state.update {
                                it.copy(
                                    notificationMessage = "Data berhasil disimpan!",
                                    isPostSuccessful = true,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            )
        } else viewModelScope.launch {
            repository.storeHeavyVehicleFuel(heavyVehicleEntity)
            _state.update {
                it.copy(
                    notificationMessage = "Data berhasil disimpan!",
                    isPostSuccessful = true
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: HeavyVehicleFuelQrScreenEvent) {
        when (event) {
            is HeavyVehicleFuelQrScreenEvent.OnNavigateForm -> _state.update {
                it.copy(currentlyScanning = event.scanOptions)
            }

            is HeavyVehicleFuelQrScreenEvent.OnHeavyVehicleIdRegistered -> {
                if (event.connectionStatus == Status.Available) {
                    checkHeavyVehicleId(event.vHId)
                } else _state.update {
                    it.copy(
                        heavyVehicleId = event.vHId,
                        currentlyScanning = ScanOptions.Driver
                    )
                }
            }

            is HeavyVehicleFuelQrScreenEvent.OnDriverIdRegistered -> {
                if (event.connectionStatus == Status.Available) {
                    checkDriverId(event.driverId)
                } else _state.update {
                    it.copy(
                        driverId = event.driverId,
                        currentlyScanning = ScanOptions.Pos
                    )
                }
            }

            is HeavyVehicleFuelQrScreenEvent.OnStationIdRegistered -> {
                if (event.connectionStatus == Status.Available) {
                    checkStationId(event.stationId)
                } else _state.update {
                    it.copy(
                        stationId = event.stationId,
                        currentlyScanning = ScanOptions.Volume
                    )
                }
            }

            is HeavyVehicleFuelQrScreenEvent.OnVolumeRegistered -> {
                if (event.volume == null) {
                    _state.update { it.copy(notificationMessage = "Maaf, Qr invalid.") }
                } else _state.update {
                    it.copy(volume = event.volume, currentlyScanning = ScanOptions.None)
                }
            }

            is HeavyVehicleFuelQrScreenEvent.OnHourmeterChange -> _state.update {
                it.copy(hourmeter = event.odometer)
            }

            HeavyVehicleFuelQrScreenEvent.OnClearHourmeter -> _state.update {
                it.copy(hourmeter = "")
            }

            is HeavyVehicleFuelQrScreenEvent.OnRemarksChange -> _state.update {
                it.copy(remarks = event.remarks)
            }

            HeavyVehicleFuelQrScreenEvent.OnClearRemarks -> _state.update {
                it.copy(remarks = "")
            }

            HeavyVehicleFuelQrScreenEvent.NotificationClear -> _state.update {
                it.copy(notificationMessage = "", isRequestFailed = FailedRequest())
            }

            is HeavyVehicleFuelQrScreenEvent.SaveHeavyVehicleFuelTransaction -> {
                if (state.value.hourmeter.isBlank()) {
                    _state.update {
                        it.copy(
                            hourmeterErrorState = ErrorTextField(
                                isError = !it.hourmeterErrorState.isError,
                                errorMessage = "Hourmeter tidak bolah kosong."
                            )
                        )
                    }
                } else {
                    val data = FuelHeavyVehicleEntity(
                        heavyVehicleId = state.value.heavyVehicleId,
                        driverId = state.value.driverId,
                        stationId = state.value.stationId,
                        gasOperatorId = state.value.userId,
                        volume = state.value.volume,
                        hourmeter = state.value.hourmeter.toDouble(),
                        remarks = state.value.remarks,
                        date = LocalDateTime.now().toString()
                    )
                    postHeavyVehicleFuel(data, event.connectionStatus)
                }
            }
        }
    }
}