package com.system.hasilkarya.core.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gas")
data class GasEntity(

    @PrimaryKey
    val truckId: String = "",
    val driverId: String = "",
    val stationId: String = "",
    val userId: String = "",
    val volume: Double = 0.0,
    val odometer: Double = 0.0,

)
