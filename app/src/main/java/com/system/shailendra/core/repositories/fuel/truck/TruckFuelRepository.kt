package com.system.shailendra.core.repositories.fuel.truck

import com.system.shailendra.core.entities.FuelTruckEntity
import com.system.shailendra.core.network.ApiServices
import com.system.shailendra.core.preferences.AppPreferences
import com.system.shailendra.truck_fuel.data.TruckFuelLogRequest
import com.system.shailendra.truck_fuel.data.TruckFuelRequest
import javax.inject.Inject

class TruckFuelRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val preferences: AppPreferences,
    private val dao: TruckFuelDao
){

    fun getUserId() = preferences.getUserId()
    fun getToken() = preferences.getToken()
    suspend fun saveFuel(fuelTruckEntity: FuelTruckEntity) = dao.saveFuel(fuelTruckEntity)
    suspend fun deleteFuel(fuelTruckEntity: FuelTruckEntity) = dao.deleteFuel(fuelTruckEntity)
    fun getFuels() = dao.getFuels()
    fun postFuels(token: String, truckFuelRequest: TruckFuelRequest) = apiServices.postFuelTruck(token, truckFuelRequest)
    fun checkDriverId(token: String, driverId: String) = apiServices.checkDriverId(driverId, token)
    fun checkTruckId(token: String, truckId: String) = apiServices.checkTruckId(truckId, token)
    fun checkStationId(token: String, stationId: String) = apiServices.checkGasStationId(stationId, token)
    fun postToLog(token: String, truckFuelRequest: TruckFuelLogRequest) = apiServices.postFuelLog(token, truckFuelRequest)
}