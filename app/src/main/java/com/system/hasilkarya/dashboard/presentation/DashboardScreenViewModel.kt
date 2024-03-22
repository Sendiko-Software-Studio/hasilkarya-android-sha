package com.system.hasilkarya.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.MaterialEntity
import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import com.system.hasilkarya.dashboard.data.PostMaterialResponse
import com.system.hasilkarya.dashboard.data.PostToLogRequest
import com.system.hasilkarya.dashboard.data.PostToLogResponse
import com.system.hasilkarya.dashboard.domain.MaterialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class DashboardScreenViewModel @Inject constructor(
    private val repository: MaterialRepository,
    preferences: AppPreferences,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    private val _token = preferences.getToken()
    private val _name = preferences.getName()
    private val _materials = repository.getMaterials()
    val connectionStatus = combine(connectionObserver.observe(), _state) { connectionStatus, state ->
        state.copy(connectionStatus = connectionStatus)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())
    val state = combine(
        _materials,
        _token,
        _name,
        _state
    ) { materials, token, name, state ->
        state.copy(
            token = token,
            name = name,
            materials = materials,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())

    private fun isTruckValid(token: String, truckId: String): Boolean {
        val request = repository.checkTruckId(token, truckId)
        return request.execute().code() == 200
    }

    private fun isDriverValid(token: String, driverId: String): Boolean {
        val request = repository.checkDriverId(token, driverId)
        return request.execute().code() == 200
    }

    private fun isStationValid(token: String, stationId: String): Boolean {
        val request = repository.checkStationId(token, stationId)
        return request.execute().code() == 200
    }

    private fun postToLog(token: String, material: MaterialEntity) {
        _state.update { it.copy(isLoading = true) }
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isTruckValid(token, material.truckId))
                message += "Truck ID is not valid, "

            if (!isDriverValid(token, material.driverId))
                message += "Driver ID is not valid, "

            if (!isStationValid(token, material.stationId))
                message += "Station ID is not valid."

            val data = PostToLogRequest(
                driverId = material.driverId,
                truckId = material.truckId,
                stationId = material.stationId,
                checkerId = material.checkerId,
                errorLog = message
            )

            val request = repository.postToLog(token, data)

            repository.checkDriverId(token, driverId = material.driverId)

            request.enqueue(
                object : Callback<PostToLogResponse> {
                    override fun onResponse(
                        call: Call<PostToLogResponse>,
                        response: Response<PostToLogResponse>
                    ) {
                        Log.i("RESPONSE_MESSAGE", "onResponse: $message")
                        _state.update { it.copy(isLoading = false) }
                        when(response.code()) {
                            200 -> viewModelScope.launch {
                                repository.deleteMaterial(material)
                                _state.update { it.copy(isPostSuccessful = true) }
                            }

                            else -> viewModelScope.launch {
                                repository.saveMaterial(material)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostToLogResponse>, t: Throwable) {
                        viewModelScope.launch { repository.saveMaterial(material) }
                        _state.update { it.copy(
                            isLoading = false,
                            isRequestFailed = FailedRequest(true),
                        ) }
                    }

                }
            )
        }
    }

    private fun checkAndPost() {
        val datas = state.value.materials
        datas.forEach {
            postMaterial(it)
        }
    }

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
        if (connectionStatus.value.connectionStatus == Status.Available) {
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
                                viewModelScope.launch {
                                    repository.deleteMaterial(materialEntity)
                                }
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                        materials = state.value.materials - materialEntity
                                    )
                                }
                            }

                            422 -> viewModelScope.launch {
                                postToLog(token, materialEntity)
                            }

                            else -> {
                                postToLog(token, materialEntity)
                                _state.update {
                                    it.copy(
                                        isRequestFailed = FailedRequest(isFailed = true)
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostMaterialResponse>, t: Throwable) {
                        viewModelScope.launch { repository.saveMaterial(materialEntity) }
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                }
            )
        } else {
            viewModelScope.launch { repository.saveMaterial(materialEntity) }
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    fun onEvent(event: DashboardScreenEvent) {
        when (event) {
            DashboardScreenEvent.ClearNotificationState -> _state.update {
                it.copy(
                    isRequestFailed = FailedRequest()
                )
            }

            DashboardScreenEvent.CheckDataAndPost -> checkAndPost()
        }
    }
}