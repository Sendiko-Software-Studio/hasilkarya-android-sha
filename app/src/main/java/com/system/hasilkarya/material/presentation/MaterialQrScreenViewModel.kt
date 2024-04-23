package com.system.hasilkarya.material.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.repositories.material.MaterialRepository
import com.system.hasilkarya.core.ui.utils.ErrorTextField
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.CheckDriverIdResponse
import com.system.hasilkarya.dashboard.data.CheckStationIdResponse
import com.system.hasilkarya.dashboard.data.CheckTruckIdResponse
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.Driver
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.None
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.Pos
import com.system.hasilkarya.material.data.PostMaterialRequest
import com.system.hasilkarya.material.data.PostMaterialResponse
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
class MaterialQrScreenViewModel @Inject constructor(
    private val repository: MaterialRepository,
    preferences: AppPreferences,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(MaterialQrScreenState())
    private val _token = preferences.getToken()
    private val _userId = preferences.getUserId()
    val connectionStatus = combine(connectionObserver.observe(), _state) { connectionStatus, state ->
        state.copy(connectionStatus = connectionStatus)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), MaterialQrScreenState())
    val state = combine(
        _token, _userId, _state
    ) { token, userId, state ->
        state.copy(
            token = token,
            userId = userId,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), MaterialQrScreenState())

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
                            it.copy(driverId = driverId, currentlyScanning = Pos)
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
                            it.copy(truckId = truckId, currentlyScanning = Driver)
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
                            it.copy(posId = stationId, currentlyScanning = None)
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

    private fun postMaterial(materialEntity: MaterialEntity, connectionStatus: Status) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val data = PostMaterialRequest(
            driverId = materialEntity.driverId,
            truckId = materialEntity.truckId,
            stationId = materialEntity.stationId,
            checkerId = materialEntity.checkerId,
            observationRatio = materialEntity.ratio.toInt(),
            remarks = materialEntity.remarks,
            date = materialEntity.date
        )
        if (connectionStatus == Status.Available) {
            val request = repository.postMaterial(token, data)
            request.enqueue(
                object : Callback<PostMaterialResponse> {
                    override fun onResponse(
                        call: Call<PostMaterialResponse>,
                        response: Response<PostMaterialResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when (response.code()) {
                            201 -> {
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                        notificationMessage = "Data tersimpan!",
                                    )
                                }
                            }

                            else -> {
                                _state.update {
                                    it.copy(
                                        notificationMessage = "Duh, ada yang salah",
                                        isRequestFailed = FailedRequest(isFailed = true),
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostMaterialResponse>, t: Throwable) {
                        viewModelScope.launch { repository.saveMaterial(materialEntity) }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                notificationMessage = "Data berhasil tersimpan!",
                                isPostSuccessful = true
                            )
                        }
                    }

                }
            )
        } else {
            viewModelScope.launch { repository.saveMaterial(materialEntity) }
            _state.update {
                it.copy(
                    notificationMessage = "Data berhasil tersimpan!",
                    isLoading = false,
                    isPostSuccessful = true
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: MaterialQrScreenEvent) {
        when (event) {
            is MaterialQrScreenEvent.OnDriverIdRegistered -> if (event.connectionStatus == Status.Available)
                checkDriverId(event.driverId)
            else _state.update { it.copy(driverId = event.driverId, currentlyScanning = Pos) }

            is MaterialQrScreenEvent.OnTruckIdRegistered -> if (event.connectionStatus == Status.Available)
                checkTruckId(event.truckId)
            else _state.update { it.copy(truckId = event.truckId, currentlyScanning = Driver) }

            is MaterialQrScreenEvent.OnPosIdRegistered -> if (event.connectionStatus == Status.Available)
                checkStationId(event.posId)
            else _state.update { it.copy(posId = event.posId, currentlyScanning = None) }

            is MaterialQrScreenEvent.OnVolumeChange -> _state.update {
                it.copy(materialVolume = event.volume)
            }

            is MaterialQrScreenEvent.OnNewRemarks -> _state.update {
                it.copy(remarks = event.remarks)
            }

            is MaterialQrScreenEvent.SaveMaterial -> {
                if (state.value.materialVolume.isBlank()){
                    _state.update {
                        it.copy(
                            materialVolumeErrorState = ErrorTextField(
                                isError = !it.materialVolumeErrorState.isError,
                                errorMessage = "Volume Material tidak boleh kosong."
                            )
                        )
                    }
                } else {

                    val data = if (event.connectionStatus != Status.Available) {
                        MaterialEntity(
                            driverId = state.value.driverId,
                            truckId = state.value.truckId,
                            stationId = state.value.posId,
                            ratio = state.value.materialVolume.toDouble(),
                            remarks = state.value.remarks,
                            checkerId = state.value.userId,
                            date = LocalDateTime.now().toString(),
                        )
                    } else {
                        MaterialEntity(
                            driverId = state.value.driverId,
                            truckId = state.value.truckId,
                            stationId = state.value.posId,
                            ratio = state.value.materialVolume.toDouble(),
                            remarks = state.value.remarks,
                            checkerId = state.value.userId,
                            date = "",
                        )
                    }
                    postMaterial(data, event.connectionStatus)
                }
            }

            MaterialQrScreenEvent.OnClearNotification -> _state.update {
                it.copy(notificationMessage = "")
            }

            is MaterialQrScreenEvent.OnClearRemarks -> _state.update {
                it.copy(remarks = "")
            }

            is MaterialQrScreenEvent.OnNavigateForm -> _state.update {
                it.copy(currentlyScanning = event.scanOptions)
            }
        }
    }
}