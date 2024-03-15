package com.system.hasilkarya.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.MaterialEntity
import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import com.system.hasilkarya.dashboard.data.PostMaterialResponse
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
    private val _name = preferences.getName()
    private val _materials = repository.getMaterials()
    private val _connectionStatus = connectionObserver.observe()
    val state = combine(
        _connectionStatus,
        _materials,
        _name,
        _state
    ) { connectionStatus, materials, name, state ->
        state.copy(
            connectionStatus = connectionStatus,
            name = name,
            materials = materials
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())

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
                                        showingForm = false
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
                                        showingForm = false
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
                                showingForm = false
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
                    showingForm = false
                )
            }
        }
    }

    fun onEvent(event: DashboardScreenEvent) {
        when (event) {
            DashboardScreenEvent.ClearNotificationState -> _state.update {
                it.copy(
                    notificationMessage = "",
                    isRequestFailed = FailedRequest()
                )
            }

            DashboardScreenEvent.CheckDataAndPost -> checkAndPost()
        }
    }
}