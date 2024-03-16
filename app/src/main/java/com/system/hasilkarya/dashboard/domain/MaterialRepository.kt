package com.system.hasilkarya.dashboard.domain

import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.dashboard.data.MaterialEntity
import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import javax.inject.Inject

class MaterialRepository @Inject constructor(
    private val dao: MaterialDao,
    private val apiServices: ApiServices
) {

    suspend fun saveMaterial(material: MaterialEntity) = dao.saveMaterial(material)

    fun postMaterial(token: String, material: PostMaterialRequest) = apiServices.postMaterial(token, material)

    fun getMaterials() = dao.getAllMaterial()

    suspend fun deleteMaterial(material: MaterialEntity) = dao.deleteMaterial(material)
}