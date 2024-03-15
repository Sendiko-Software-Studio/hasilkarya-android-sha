package com.system.hasilkarya.dashboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material")
data class MaterialEntity(
    @PrimaryKey
    val driverId: String,
    val truckId: String,
    val stationId: String,
    val checkerId: String,
    val ratio: Double,
    val remarks: String,
)
