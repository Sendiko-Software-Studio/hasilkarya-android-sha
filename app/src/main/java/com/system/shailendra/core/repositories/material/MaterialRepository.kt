package com.system.shailendra.core.repositories.material

import com.system.shailendra.core.entities.MaterialEntity
import com.system.shailendra.core.network.ApiServices
import com.system.shailendra.core.preferences.AppPreferences
import com.system.shailendra.material.data.PostMaterialLogRequest
import com.system.shailendra.material.data.PostMaterialRequest
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
    fun checkTruckId(token: String, truckId: String) = apiServices.checkTruckId(truckId, token)
    fun checkStationId(token: String, stationId: String) = apiServices.checkMineStationId(stationId, token)
}