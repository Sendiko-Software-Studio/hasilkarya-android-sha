package com.system.hasilkarya.core.repositories

import com.system.hasilkarya.core.entities.GasEntity
import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.dashboard.domain.GasDao
import com.system.hasilkarya.gas.data.TruckGasRequest
import javax.inject.Inject

class GasRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val preferences: AppPreferences,
    private val dao: GasDao
){

    fun getUserId() = preferences.getUserId()
    fun getToken() = preferences.getToken()
    suspend fun store(gasEntity: GasEntity) = dao.saveGas(gasEntity)
    suspend fun deleteGas(gasEntity: GasEntity) = dao.deleteGas(gasEntity)
    fun getGases() = dao.getGases()
    fun postGas(token: String, truckGasRequest: TruckGasRequest) = apiServices.postFuelTruck(token, truckGasRequest)
    fun checkDriverId(token: String, driverId: String) = apiServices.checkDriverId(driverId, token)
    fun checkTruckId(token: String, truckId: String) = apiServices.checkTruckId(truckId, token)
    fun checkStationId(token: String, stationId: String) = apiServices.checkStationId(stationId, token)
}