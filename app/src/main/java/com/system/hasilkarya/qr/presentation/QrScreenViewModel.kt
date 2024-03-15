package com.system.hasilkarya.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.MaterialEntity
import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import com.system.hasilkarya.dashboard.data.PostMaterialResponse
import com.system.hasilkarya.dashboard.domain.MaterialRepository
import com.system.hasilkarya.dashboard.presentation.ScanOptions
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
class QrScreenViewModel @Inject constructor(
    private val repository: MaterialRepository,
    preferences: AppPreferences,
): ViewModel() {

    private val _state = MutableStateFlow(QrScreenState())
    private val _token = preferences.getToken()
    private val _userId = preferences.getUserId()
    val state = combine(
        _token, _userId, _state
    ){ token, userId, state ->
        state.copy(
            token = token,
            userId = userId,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), QrScreenState())

    private fun postMaterial(materialEntity: MaterialEntity) {
        _state.update { it.copy(isLoading = true) }
        val token = "Bearer ${state.value.token}"
        val data = PostMaterialRequest(
            driverId = materialEntity.driverId,
            truckId = materialEntity.truckId,
            stationId = materialEntity.stationId,
            checkerId = materialEntity.checkerId,
            ratio = materialEntity.ratio,
            remarks = materialEntity.remarks,
        )
        if (state.value.connectionStatus == Status.Available) {
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
                                viewModelScope.launch {
                                    repository.deleteMaterial(materialEntity)
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

    fun onEvent(event: QrScreenEvent) {
        when(event){
            is QrScreenEvent.OnDriverIdRegistered -> _state.update {
                it.copy(driverId = event.driverId, currentlyScanning = ScanOptions.Truck)
            }
            is QrScreenEvent.OnTruckIdRegistered -> _state.update {
                it.copy(truckId = event.truckId, currentlyScanning = ScanOptions.Pos)
            }
            is QrScreenEvent.OnPosIdRegistered -> _state.update {
                it.copy(posId = event.posId, currentlyScanning = ScanOptions.None)
            }
            is QrScreenEvent.OnSelectedRatio -> _state.update {
                it.copy(observationRatioPercentage = event.ratio)
            }
            is QrScreenEvent.OnNewRemarks -> _state.update {
                it.copy(remarks = event.remarks)
            }
            QrScreenEvent.SaveMaterial -> {
                val data = MaterialEntity(
                    driverId = state.value.driverId,
                    truckId = state.value.truckId,
                    stationId = state.value.posId,
                    ratio = state.value.observationRatioPercentage,
                    remarks = state.value.remarks,
                    checkerId = state.value.userId,
                )
                postMaterial(data)
            }

            QrScreenEvent.OnClearNotification -> _state.update {
                it.copy(notificationMessage = "")
            }
        }
    }
}