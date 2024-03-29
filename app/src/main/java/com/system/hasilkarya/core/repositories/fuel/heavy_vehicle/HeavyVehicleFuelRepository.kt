package com.system.hasilkarya.core.repositories.fuel.heavy_vehicle

import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.heavy_vehicle_fuel.data.HeavyVehicleFuelRequest
import javax.inject.Inject

class HeavyVehicleFuelRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val preferences: AppPreferences,
    private val dao: HeavyVehicleDao
) {

    fun getUserId() = preferences.getUserId()
    fun getToken() = preferences.getToken()
    suspend fun storeHeavyVehicleFuel(fuelHeavyVehicleEntity: FuelHeavyVehicleEntity) = dao.storeHeavyVehicleFuel(fuelHeavyVehicleEntity)
    suspend fun deleteHeavyVehicleFuel(fuelHeavyVehicleEntity: FuelHeavyVehicleEntity) = dao.deleteHeavyVehicleFuel(fuelHeavyVehicleEntity)
    fun getHeavyVehicleFuels() = dao.getHeavyVehicleFuels()
    fun checkHeavyVehicleId(token: String, heavyVehicleId: String) = apiServices.checkHeavyVehicleId(heavyVehicleId, token)
    fun checkDriverId(token: String, driverId: String) = apiServices.checkDriverId(driverId, token)
    fun checkStationId(token: String, stationId: String) = apiServices.checkStationId(stationId, token)
    fun postHeavyVehicleFuel(token: String, heavyVehicleFuelRequest: HeavyVehicleFuelRequest) = apiServices.postFuelHeavyVehicle(token, heavyVehicleFuelRequest)
}