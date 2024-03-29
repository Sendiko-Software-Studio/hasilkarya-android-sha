package com.system.hasilkarya.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleFuelRepository
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelRepository
import com.system.hasilkarya.core.repositories.material.MaterialRepository
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.MaterialLogRequest
import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import com.system.hasilkarya.dashboard.data.PostMaterialResponse
import com.system.hasilkarya.dashboard.data.PostToLogResponse
import com.system.hasilkarya.heavy_vehicle_fuel.data.HeavyVehicleFuelRequest
import com.system.hasilkarya.heavy_vehicle_fuel.data.HeavyVehicleFuelResponse
import com.system.hasilkarya.truck_fuel.data.TruckFuelLogRequest
import com.system.hasilkarya.truck_fuel.data.TruckFuelLogResponse
import com.system.hasilkarya.truck_fuel.data.TruckFuelRequest
import com.system.hasilkarya.truck_fuel.data.TruckFuelResponse
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
    private val materialRepository: MaterialRepository,
    private val truckFuelRepository: TruckFuelRepository,
    private val heavyVehicleFuelRepository: HeavyVehicleFuelRepository,
    connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    private val _token = materialRepository.getToken()
    private val _name = materialRepository.getName()
    private val _role = materialRepository.getRole()
    private val _materials = materialRepository.getMaterials()
    private val _fuels = truckFuelRepository.getFuels()
    private val _heavyFuels = heavyVehicleFuelRepository.getHeavyVehicleFuels()
    val connectionStatus = combine(connectionObserver.observe(), _state) { connectionStatus, state ->
        state.copy(connectionStatus = connectionStatus)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())
    private val _dataList = combine(
        _materials,
        _fuels,
        _heavyFuels,
        _state
    ) { materials, fuels, heavyFuels, state ->
        state.copy(
            materials = materials,
            fuels = fuels,
            heavyFuels = heavyFuels
        )
    }
    val state = combine(
        _token,
        _name,
        _role,
        _dataList,
        _state
    ) { token, name, role, dataList, state ->
        state.copy(
            token = token,
            name = name,
            role = role,
            materials = dataList.materials,
            fuels = dataList.fuels,
            heavyFuels = dataList.heavyFuels
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())

    private fun isTruckValid(token: String, truckId: String): Boolean {
        val request = materialRepository.checkTruckId(token, truckId)
        return request.execute().code() == 200
    }

    private fun isDriverValid(token: String, driverId: String): Boolean {
        val request = materialRepository.checkDriverId(token, driverId)
        return request.execute().code() == 200
    }

    private fun isStationValid(token: String, stationId: String): Boolean {
        val request = materialRepository.checkStationId(token, stationId)
        return request.execute().code() == 200
    }
    private fun postFuelToLog(token: String, fuelTruckEntity: FuelTruckEntity) {
        _state.update { it.copy(isLoading = true) }
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isTruckValid(token, fuelTruckEntity.truckId))
                message += "Truck ID is not valid, "

            if (!isDriverValid(token, fuelTruckEntity.driverId))
                message += "Driver ID is not valid, "

            if (!isStationValid(token, fuelTruckEntity.stationId))
                message += "Station ID is not valid."

            val data = TruckFuelLogRequest(
                truckId = fuelTruckEntity.truckId,
                driverId = fuelTruckEntity.driverId,
                stationId = fuelTruckEntity.stationId,
                volume = fuelTruckEntity.volume,
                odometer = fuelTruckEntity.odometer,
                gasOperatorId = fuelTruckEntity.userId,
                errorLog = message,
                remarks = fuelTruckEntity.remarks
            )

            val request = truckFuelRepository.postToLog(token, data)

            request.enqueue(
                object : Callback<TruckFuelLogResponse> {
                    override fun onResponse(
                        call: Call<TruckFuelLogResponse>,
                        response: Response<TruckFuelLogResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when(response.code()) {
                            200 -> {
                                viewModelScope.launch { truckFuelRepository.deleteFuel(fuelTruckEntity) }
                                _state.update {
                                    it.copy(isPostSuccessful = true)
                                }
                            }
                            else -> viewModelScope.launch {
                                truckFuelRepository.saveFuel(fuelTruckEntity)
                            }
                        }
                    }

                    override fun onFailure(call: Call<TruckFuelLogResponse>, t: Throwable) {
                        viewModelScope.launch { truckFuelRepository.saveFuel(fuelTruckEntity) }
                        _state.update {
                            it.copy(
                                isRequestFailed = FailedRequest(isFailed = true)
                            )
                        }
                    }

                }
            )
        }
    }

    private fun postMaterialToLog(token: String, material: MaterialEntity) {
        _state.update { it.copy(isLoading = true) }
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isTruckValid(token, material.truckId))
                message += "Truck ID is not valid, "

            if (!isDriverValid(token, material.driverId))
                message += "Driver ID is not valid, "

            if (!isStationValid(token, material.stationId))
                message += "Station ID is not valid."

            val data = MaterialLogRequest(
                driverId = material.driverId,
                truckId = material.truckId,
                stationId = material.stationId,
                checkerId = material.checkerId,
                errorLog = message
            )

            val request = materialRepository.postToLog(token, data)

            request.enqueue(
                object : Callback<PostToLogResponse> {
                    override fun onResponse(
                        call: Call<PostToLogResponse>,
                        response: Response<PostToLogResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when(response.code()) {
                            200 -> viewModelScope.launch {
                                materialRepository.deleteMaterial(material)
                                _state.update { it.copy(isPostSuccessful = true) }
                            }

                            else -> viewModelScope.launch {
                                materialRepository.saveMaterial(material)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostToLogResponse>, t: Throwable) {
                        viewModelScope.launch { materialRepository.saveMaterial(material) }
                        _state.update { it.copy(
                            isLoading = false,
                            isRequestFailed = FailedRequest(true),
                        ) }
                    }

                }
            )
        }
    }

    private fun checkAndPostMaterials() {
        val materials = state.value.materials
        materials.forEach {
            postMaterial(it)
        }
    }

    private fun checkAndPostFuels() {
        val fuels = state.value.fuels
        fuels.forEach {
            postTruckFuel(it)
        }
    }

    private fun checkAndPostHeavyFuels() {
        Log.i("DEBUG", "checkAndPostHeavyFuels: active")
        val fuels = state.value.heavyFuels
        fuels.forEach {
            postHeavyVehicleFuel(it)
        }
    }

    private fun postHeavyVehicleFuel(heavyVehicleEntity: FuelHeavyVehicleEntity) {
        val token = "Bearer ${state.value.token}"
        val data = HeavyVehicleFuelRequest(
            heavyVehicleId = heavyVehicleEntity.heavyVehicleId,
            driverId = heavyVehicleEntity.driverId,
            stationId = heavyVehicleEntity.stationId,
            gasOperatorId = heavyVehicleEntity.gasOperatorId,
            volume = heavyVehicleEntity.volume,
            hourmeter = heavyVehicleEntity.hourmeter,
            remarks = heavyVehicleEntity.remarks
        )
        if (connectionStatus.value.connectionStatus == Status.Available) {
            _state.update { it.copy(isLoading = true) }
            val request = heavyVehicleFuelRepository.postHeavyVehicleFuel(token, data)
            request.enqueue(
                object : Callback<HeavyVehicleFuelResponse> {
                    override fun onResponse(
                        call: Call<HeavyVehicleFuelResponse>,
                        response: Response<HeavyVehicleFuelResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when(response.code()) {
                            201 -> {
                                viewModelScope.launch { heavyVehicleFuelRepository.deleteHeavyVehicleFuel(heavyVehicleEntity) }
                                _state.update { it.copy(isPostSuccessful = true) }
                            }
                            else -> viewModelScope.launch {
                                heavyVehicleFuelRepository.deleteHeavyVehicleFuel(heavyVehicleEntity)
                                _state.update { it.copy(isPostSuccessful = true, heavyFuels = it.heavyFuels - heavyVehicleEntity) }
                            }
                        }
                    }

                    override fun onFailure(call: Call<HeavyVehicleFuelResponse>, t: Throwable) {
                        viewModelScope.launch {
                            heavyVehicleFuelRepository.storeHeavyVehicleFuel(heavyVehicleEntity)
                            _state.update { it.copy(isPostSuccessful = true, isLoading = false) }
                        }
                    }
                }
            )
        } else viewModelScope.launch {
            heavyVehicleFuelRepository.storeHeavyVehicleFuel(heavyVehicleEntity)
            _state.update { it.copy(isPostSuccessful = true) }
        }
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
        if (connectionStatus.value.connectionStatus == Status.Available) {
            val request = truckFuelRepository.postFuels(token, data)
            request.enqueue(
                object : Callback<TruckFuelResponse> {
                    override fun onResponse(
                        call: Call<TruckFuelResponse>,
                        response: Response<TruckFuelResponse>
                    ) {
                        _state.update { it.copy(isLoading = false) }
                        when (response.code()) {
                            201 -> {
                                viewModelScope.launch {
                                    truckFuelRepository.deleteFuel(fuelTruckEntity)
                                }
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                    )
                                }
                            }

                            422 -> viewModelScope.launch {
                                postFuelToLog(token, fuelTruckEntity)
                            }

                            else -> {
                                postFuelToLog(token, fuelTruckEntity)
                                _state.update {
                                    it.copy(
                                        isRequestFailed = FailedRequest(isFailed = true)
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<TruckFuelResponse>, t: Throwable) {
                        viewModelScope.launch { truckFuelRepository.saveFuel(fuelTruckEntity) }
                        _state.update {
                            it.copy(
                                isPostSuccessful = true,
                            )
                        }
                    }

                }
            )
        } else {
            viewModelScope.launch { truckFuelRepository.saveFuel(fuelTruckEntity) }
            _state.update {
                it.copy(
                    isPostSuccessful = true,
                )
            }
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
            val request = materialRepository.postMaterial(token, data)
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
                                    materialRepository.deleteMaterial(materialEntity)
                                }
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                        materials = state.value.materials - materialEntity
                                    )
                                }
                            }

                            422 -> viewModelScope.launch {
                                postMaterialToLog(token, materialEntity)
                            }

                            else -> {
                                postMaterialToLog(token, materialEntity)
                                _state.update {
                                    it.copy(
                                        isRequestFailed = FailedRequest(isFailed = true)
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostMaterialResponse>, t: Throwable) {
                        viewModelScope.launch { materialRepository.saveMaterial(materialEntity) }
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                }
            )
        } else {
            viewModelScope.launch { materialRepository.saveMaterial(materialEntity) }
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

            DashboardScreenEvent.CheckDataAndPost -> {
                Log.i("DEBUG", "onEvent: active")
                checkAndPostMaterials()
                checkAndPostFuels()
                checkAndPostHeavyFuels()
            }
        }
    }
}