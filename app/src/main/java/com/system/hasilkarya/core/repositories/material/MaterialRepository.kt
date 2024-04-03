package com.system.hasilkarya.core.repositories.material

import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.material.data.PostMaterialLogRequest
import com.system.hasilkarya.material.data.PostMaterialRequest
import javax.inject.Inject

class MaterialRepository @Inject constructor(
    private val dao: MaterialDao,
    private val apiServices: ApiServices,
    private val preferences: AppPreferences,
) {

    suspend fun saveMaterial(material: MaterialEntity) = dao.saveMaterial(material)
    suspend fun deleteMaterial(material: MaterialEntity) = dao.deleteMaterial(material)
    fun postMaterial(token: String, material: PostMaterialRequest) = apiServices.postMaterial(token, material)
    fun postToLog(token: String, material: PostMaterialLogRequest) = apiServices.postToLog(token, material)
    fun getMaterials() = dao.getAllMaterial()
    fun checkDriverId(token: String, driverId: String) = apiServices.checkDriverId(driverId, token)
    fun checkTruckId(token: String, truckId: String) = apiServices.checkTruckId(truckId, token)
    fun checkStationId(token: String, stationId: String) = apiServices.checkStationId(stationId, token)
    fun getToken() = preferences.getToken()
    fun getName() = preferences.getName()
    fun getRole() = preferences.getRole()
}