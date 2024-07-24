package com.system.shailendra.core.repositories.station

import com.system.shailendra.core.entities.StationEntity
import com.system.shailendra.core.network.ApiServices

class StationRepository(private val dao: StationDao, private val apiServices: ApiServices) {

    suspend fun saveStation(station: StationEntity) = dao.insert(station)

    suspend fun updateStation(station: StationEntity) = dao.update(station)

    suspend fun deleteStation(station: StationEntity) = dao.delete(station)

    fun getStation(id: Int) = dao.getById(id)

    fun getAllStations() = dao.getAll()

    fun getStationFromApi(id: String, token: String) = apiServices.getStation(id, token)

    fun checkMineStationId(id: String, token: String) = apiServices.checkMineStationId(id, token)

    fun checkGasStationId(id: String, token: String) = apiServices.checkGasStationId(id, token)

}