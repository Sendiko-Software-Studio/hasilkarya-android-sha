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
import com.system.hasilkarya.dashboard.domain.MaterialRepository
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

    private fun postToLog(token: String, material: PostMaterialRequest) {
        _state.update { it.copy(isLoading = true) }
        val data = PostToLogRequest(
            driverId = material.driverId,
            truckId = material.truckId,
            stationId = material.stationId,
            checkerId = material.checkerId,
            errorLog = material.remarks
        )
        val request = repository.postToLog(token, data)

        request.enqueue(
            object : Callback<PostMaterialResponse> {
                override fun onResponse(
                    call: Call<PostMaterialResponse>,
                    response: Response<PostMaterialResponse>
                ) {
                    _state.update { it.copy(
                        isLoading = false,
                        isPostSuccessful = true,
                    ) }
                }

                override fun onFailure(call: Call<PostMaterialResponse>, t: Throwable) {
                    _state.update { it.copy(
                        isLoading = false,
                        isRequestFailed = FailedRequest(true),
                    ) }
                }

            }
        )
    }

    private fun checkAndPost() {
        val datas = state.value.materials
        Log.i("MATERIALS", "checkAndPost: $datas")
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
                        Log.i("STATUS", "onResponse: try")
                        _state.update { it.copy(isLoading = false) }
                        when (response.code()) {
                            201 -> {
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                        materials = state.value.materials - materialEntity
                                    )
                                }
                            }

                            405 -> viewModelScope.launch {
                                postToLog(token, data)
                                repository.deleteMaterial(materialEntity)
                            }


                            422 -> viewModelScope.launch {
                                postToLog(token, data)
                                repository.deleteMaterial(materialEntity)
                            }

                            else -> {
                                postToLog(token, data)
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