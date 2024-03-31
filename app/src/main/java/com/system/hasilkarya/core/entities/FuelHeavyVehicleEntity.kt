package com.system.hasilkarya.core.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_heavy_vehicle")
data class FuelHeavyVehicleEntity(

    @PrimaryKey
    val heavyVehicleId: String,
    val stationId: String,
    val driverId: String,
    val gasOperatorId: String,
    val volume: Double,
    val hourmeter: Double,
    val remarks: String,
)