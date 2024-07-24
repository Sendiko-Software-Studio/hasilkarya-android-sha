package com.system.shailendra.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.shailendra.core.entities.FuelHeavyVehicleEntity
import com.system.shailendra.core.entities.FuelTruckEntity
import com.system.shailendra.core.entities.MaterialEntity
import com.system.shailendra.core.network.NetworkConnectivityObserver
import com.system.shailendra.core.network.Status
import com.system.shailendra.core.repositories.fuel.heavy_vehicle.HeavyVehicleFuelRepository
import com.system.shailendra.core.repositories.fuel.truck.TruckFuelRepository
import com.system.shailendra.core.repositories.material.MaterialRepository
import com.system.shailendra.core.repositories.station.StationRepository
import com.system.shailendra.core.repositories.user.UserRepository
import com.system.shailendra.core.ui.utils.FailedRequest
import com.system.shailendra.dashboard.data.CheckTokenResponse
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelLogRequest
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelRequest
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelResponse
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleLogResponse
import com.system.shailendra.login.data.LoginRequest
import com.system.shailendra.login.data.LoginResponse
import com.system.shailendra.material.data.PostMaterialLogRequest
import com.system.shailendra.material.data.PostMaterialRequest
import com.system.shailendra.material.data.PostMaterialResponse
import com.system.shailendra.material.data.PostToLogResponse
import com.system.shailendra.truck_fuel.data.TruckFuelLogRequest
import com.system.shailendra.truck_fuel.data.TruckFuelLogResponse
import com.system.shailendra.truck_fuel.data.TruckFuelRequest
import com.system.shailendra.truck_fuel.data.TruckFuelResponse
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
class DashboardScreenViewModel @Inject constructor(
    private val materialRepository: MaterialRepository,
    private val truckFuelRepository: TruckFuelRepository,
    private val heavyVehicleFuelRepository: HeavyVehicleFuelRepository,
    stationRepository: StationRepository,
    private val userRepository: UserRepository,
    private val connectionObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    private val _token = userRepository.getToken()
    private val _name = userRepository.getName()
    private val _role = userRepository.getRole()
    private val _email = userRepository.getEmail()
    private val _password = userRepository.getPassword()
    private val _materials = materialRepository.getMaterials()
    private val _fuels = truckFuelRepository.getFuels()
    private val _heavyFuels = heavyVehicleFuelRepository.getHeavyVehicleFuels()
    private val _stations = stationRepository.getAllStations()
    val connectionStatus =
        combine(connectionObserver.observe(), _state) { connectionStatus, state ->
            state.copy(connectionStatus = connectionStatus)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())
    private val _dataList = combine(
        _materials,
        _fuels,
        _heavyFuels,
        _stations,
        _state
    ) { materials, fuels, heavyFuels, stations, state ->
        state.copy(
            materials = materials,
            fuels = fuels,
            heavyFuels = heavyFuels,
            totalData = materials.size + fuels.size + heavyFuels.size,
            stations = stations
        )
    }
    private val _userEmailPass =
        combine(_name, _email, _password, _state) { name, email, password, state ->
            state.copy(
                name = name,
                email = email,
                password = password
            )
        }
    private val _userState =
        combine(_token, _userEmailPass, _role, _state) { token, userEmailPass, role, state ->
            state.copy(
                token = token,
                name = userEmailPass.name,
                role = role,
                email = userEmailPass.email,
                password = userEmailPass.password
            )
        }
    val state = combine(_userState, _dataList, _state) { userState, dataList, state ->
        state.copy(
            token = userState.token,
            name = userState.name,
            role = userState.role,
            email = userState.email,
            password = userState.password,
            materials = dataList.materials,
            fuels = dataList.fuels,
            heavyFuels = dataList.heavyFuels,
            totalData = dataList.totalData,
            stations = dataList.stations,
            activeStation = dataList.stations.firstOrNull()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), DashboardScreenState())

    init {
        viewModelScope.launch {
            connectionObserver.observe().collect{ status ->
                _token.collect{ token ->
                    checkToken(token = "Bearer $token", connectionStatus = status)
                }
            }
        }
    }

    // checking token
    private fun checkToken(token: String, connectionStatus: Status) {
        if (connectionStatus == Status.Available) {
            _state.update { it.copy(isConnecting = true) }
            val request = userRepository.checkToken(token)
            request.enqueue(
                object : Callback<CheckTokenResponse> {
                    override fun onResponse(
                        call: Call<CheckTokenResponse>,
                        response: Response<CheckTokenResponse>
                    ) {
                        _state.update { it.copy(isConnecting = false) }
                        when (response.code()) {
                            401 -> {
                                login(email = state.value.email, password = state.value.password, connectionStatus)
                            }
                            200 -> {
                                _state.update {
                                    it.copy(isTokenExpired = false)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CheckTokenResponse>, t: Throwable) {
                        _state.update { it.copy(isConnecting = false) }
                    }

                }
            )
        }
    }

    private fun login(email: String, password: String, connectionStatus: Status) {
        if (connectionStatus == Status.Available){
            _state.update { it.copy(isConnecting = true) }
            val data = LoginRequest(
                email = email,
                password = password
            )
            val request = userRepository.login(data)
            request.enqueue(
                object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        _state.update { it.copy(isConnecting = false) }
                        if (response.code() != 200) {
                            _state.update { it.copy(notificationMessage = "Maaf, aplikasi tidak bisa terhubung ke server.") }
                        } else {
                            viewModelScope.launch {
                                userRepository.saveToken(response.body()!!.token)
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        _state.update {
                            it.copy(
                                isConnecting = false,
                                notificationMessage = "Maaf, aplikasi tidak bisa terhubung ke server."
                            )
                        }
                    }

                }
            )
        } else
            _state.update { it.copy(notificationMessage = "Maaf, aplikasi tidak bisa terhubung ke server.") }
    }

    // checking ids
    private fun isHeavyVehicleValid(token: String, heavyVehicleId: String): Boolean {
        val request = heavyVehicleFuelRepository.checkHeavyVehicleId(token, heavyVehicleId)
        return request.execute().code() == 200
    }

    private fun isTruckValid(token: String, truckId: String): Boolean {
        val request = materialRepository.checkTruckId(token, truckId)
        return request.execute().code() == 200
    }

    private fun isStationValid(token: String, stationId: String): Boolean {
        val request = materialRepository.checkStationId(token, stationId)
        return request.execute().code() == 200
    }

    // checking && posting offline datas
    private fun checkAndPostDatas() {
        _state.update { it.copy(isUploading = true) }
        Log.i("CHECK", "checkAndPostDatas: ${state.value.materials.size}")
        Log.i("CHECK", "checkAndPostDatas: ${state.value.isUploading}")
        val materials = state.value.materials
        materials.forEach {
            if (it.isUploaded == "true"){
                viewModelScope.launch {
                    materialRepository.deleteMaterial(it)
                }
                return@forEach
            }
            viewModelScope.launch {
                val data = MaterialEntity(
                    id = it.id,
                    driverId = it.driverId,
                    truckId = it.truckId,
                    stationId = it.stationId,
                    checkerId = it.checkerId,
                    ratio = it.ratio,
                    remarks = it.remarks,
                    isUploaded = "true"
                )
                materialRepository.saveMaterial(data)
                delay(1000)
                postMaterial(it)
            }
        }
        val fuels = state.value.fuels
        fuels.forEach {
            postTruckFuel(it)
        }
        val heavyFuels = state.value.heavyFuels
        heavyFuels.forEach {
            postHeavyVehicleFuel(it)
        }
        _state.update { it.copy(isUploading = false) }
    }

    // post material and log
    private fun postMaterial(materialEntity: MaterialEntity) {
        val token = "Bearer ${state.value.token}"
        val data = PostMaterialRequest(
            driverId = materialEntity.driverId,
            truckId = materialEntity.truckId,
            stationId = materialEntity.stationId,
            checkerId = materialEntity.checkerId,
            observationRatio = materialEntity.ratio,
            remarks = materialEntity.remarks,
            date = materialEntity.date
        )
        val request = materialRepository.postMaterial(token, data)
        request.enqueue(
            object : Callback<PostMaterialResponse> {
                override fun onResponse(
                    call: Call<PostMaterialResponse>,
                    response: Response<PostMaterialResponse>
                ) {
                    when (response.code()) {
                        201 -> {
                            viewModelScope.launch {
                                materialRepository.deleteMaterial(materialEntity)
                            }
                            _state.update {
                                it.copy(
                                    isPostSuccessful = true,
                                    materials = state.value.materials
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

                }

            }
        )
    }

    private fun postMaterialToLog(token: String, material: MaterialEntity) {
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isTruckValid(token, material.truckId))
                message += "Truck ID is not valid, "

            if (!isStationValid(token, material.stationId))
                message += "Station ID is not valid."

            val data = PostMaterialLogRequest(
                driverId = material.driverId,
                truckId = material.truckId,
                stationId = material.stationId,
                checkerId = material.checkerId,
                errorLog = message,
                remarks = material.remarks,
                date = material.date,
                ratio = material.ratio.toString()
            )

            val request = materialRepository.postToLog(token, data)

            request.enqueue(
                object : Callback<PostToLogResponse> {
                    override fun onResponse(
                        call: Call<PostToLogResponse>,
                        response: Response<PostToLogResponse>
                    ) {
                        when (response.code()) {
                            200 -> viewModelScope.launch {
                                materialRepository.deleteMaterial(material)
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                        materials = state.value.materials
                                    )
                                }
                            }

                            else -> viewModelScope.launch {
                                materialRepository.saveMaterial(material)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostToLogResponse>, t: Throwable) {
                        viewModelScope.launch {
                            materialRepository.saveMaterial(material)
                        }
                        _state.update {
                            it.copy(
                                isRequestFailed = FailedRequest(true),
                            )
                        }
                    }

                }
            )
        }
    }

    // post fuel truck and log
    private fun postTruckFuel(fuelTruckEntity: FuelTruckEntity) {
        val token = "Bearer ${state.value.token}"
        val data = TruckFuelRequest(
            truckId = fuelTruckEntity.truckId,
            driverId = fuelTruckEntity.driverId,
            stationId = fuelTruckEntity.stationId,
            volume = fuelTruckEntity.volume,
            odometer = fuelTruckEntity.odometer,
            gasOperatorId = fuelTruckEntity.userId,
            remarks = fuelTruckEntity.remarks,
            date = fuelTruckEntity.date
        )
        if (connectionStatus.value.connectionStatus == Status.Available) {
            val request = truckFuelRepository.postFuels(token, data)
            request.enqueue(
                object : Callback<TruckFuelResponse> {
                    override fun onResponse(
                        call: Call<TruckFuelResponse>,
                        response: Response<TruckFuelResponse>
                    ) {
                        when (response.code()) {
                            201 -> {
                                viewModelScope.launch {
                                    truckFuelRepository.deleteFuel(fuelTruckEntity)
                                    _state.update {
                                        it.copy(fuels = it.fuels)
                                    }
                                }
                                _state.update {
                                    it.copy(
                                        isPostSuccessful = true,
                                    )
                                }
                            }

                            422 -> viewModelScope.launch {
                                postTruckFuelToLog(token, fuelTruckEntity)
                            }

                            else -> {
                                postTruckFuelToLog(token, fuelTruckEntity)
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

    private fun postTruckFuelToLog(token: String, fuelTruckEntity: FuelTruckEntity) {
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isTruckValid(token, fuelTruckEntity.truckId))
                message += "Truck ID is not valid, "


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
                remarks = fuelTruckEntity.remarks,
                date = fuelTruckEntity.date
            )

            val request = truckFuelRepository.postToLog(token, data)

            request.enqueue(
                object : Callback<TruckFuelLogResponse> {
                    override fun onResponse(
                        call: Call<TruckFuelLogResponse>,
                        response: Response<TruckFuelLogResponse>
                    ) {
                        when (response.code()) {
                            200 -> {
                                viewModelScope.launch {
                                    truckFuelRepository.deleteFuel(
                                        fuelTruckEntity
                                    )
                                }
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

    // post fuel heavy vehicle and log
    private fun postHeavyVehicleFuel(heavyVehicleEntity: FuelHeavyVehicleEntity) {
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
        if (connectionStatus.value.connectionStatus == Status.Available) {
            val request = heavyVehicleFuelRepository.postHeavyVehicleFuel(token, data)
            request.enqueue(
                object : Callback<HeavyVehicleFuelResponse> {
                    override fun onResponse(
                        call: Call<HeavyVehicleFuelResponse>,
                        response: Response<HeavyVehicleFuelResponse>
                    ) {
                        when (response.code()) {
                            201 -> {
                                viewModelScope.launch {
                                    heavyVehicleFuelRepository.deleteHeavyVehicleFuel(
                                        heavyVehicleEntity
                                    )
                                }
                                _state.update { it.copy(isPostSuccessful = true) }
                            }

                            else -> viewModelScope.launch {
                                postHeavyVehicleFuelLog(heavyVehicleEntity)
                                _state.update { it.copy(isPostSuccessful = true) }
                            }
                        }
                    }

                    override fun onFailure(call: Call<HeavyVehicleFuelResponse>, t: Throwable) {
                        viewModelScope.launch {
                            heavyVehicleFuelRepository.storeHeavyVehicleFuel(heavyVehicleEntity)
                            _state.update { it.copy(isPostSuccessful = true) }
                        }
                    }
                }
            )
        } else viewModelScope.launch {
            heavyVehicleFuelRepository.storeHeavyVehicleFuel(heavyVehicleEntity)
            _state.update { it.copy(isPostSuccessful = true) }
        }
    }

    private fun postHeavyVehicleFuelLog(heavyVehicleEntity: FuelHeavyVehicleEntity) {
        val token = "Bearer ${state.value.token}"
        var message = ""
        viewModelScope.launch(Dispatchers.IO) {
            if (!isHeavyVehicleValid(token, heavyVehicleEntity.heavyVehicleId)) {
                message = "Heavy Vehicle ID is not valid, "
            }
            if (!isStationValid(token, heavyVehicleEntity.stationId)) {
                message = "Station ID is not valid."
            }

            val data = HeavyVehicleFuelLogRequest(
                heavyVehicleId = heavyVehicleEntity.heavyVehicleId,
                driverId = heavyVehicleEntity.driverId,
                stationId = heavyVehicleEntity.stationId,
                gasOperatorId = heavyVehicleEntity.gasOperatorId,
                volume = heavyVehicleEntity.volume,
                hourmeter = heavyVehicleEntity.hourmeter,
                remarks = heavyVehicleEntity.remarks,
                errorLog = message,
                date = heavyVehicleEntity.date
            )

            val request = heavyVehicleFuelRepository.postHeavyVehicleFuelLog(token, data)

            request.enqueue(
                object : Callback<HeavyVehicleLogResponse> {
                    override fun onResponse(
                        call: Call<HeavyVehicleLogResponse>,
                        response: Response<HeavyVehicleLogResponse>
                    ) {
                        when (response.code()) {
                            200 -> viewModelScope.launch {
                                heavyVehicleFuelRepository.deleteHeavyVehicleFuel(heavyVehicleEntity)
                                _state.update { it.copy(isPostSuccessful = true) }
                            }

                            else -> viewModelScope.launch {
                                heavyVehicleFuelRepository.storeHeavyVehicleFuel(heavyVehicleEntity)
                            }
                        }

                    }

                    override fun onFailure(call: Call<HeavyVehicleLogResponse>, t: Throwable) {
                        viewModelScope.launch {
                            heavyVehicleFuelRepository.storeHeavyVehicleFuel(
                                heavyVehicleEntity
                            )
                        }
                    }

                }
            )
        }
    }

    // state related methods
    private fun clearNotificationState() {
        _state.update { it.copy(isRequestFailed = FailedRequest()) }
    }

    // onEvent
    fun onEvent(event: DashboardScreenEvent) {
        when (event) {
            DashboardScreenEvent.ClearNotificationState -> clearNotificationState()

            DashboardScreenEvent.CheckDataAndPost -> viewModelScope.launch(Dispatchers.IO) {
                checkAndPostDatas()
            }

            DashboardScreenEvent.RetryLogin -> {
                viewModelScope.launch {
                    connectionObserver.observe().collect { status ->
                        login(state.value.email, state.value.password, status)
                    }
                }
            }

            is DashboardScreenEvent.CheckToken -> {
                checkToken(state.value.token, event.connectionStatus)
            }
        }
    }
}