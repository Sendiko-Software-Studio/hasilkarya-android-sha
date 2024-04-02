package com.system.hasilkarya.material.data

import com.google.gson.annotations.SerializedName

data class PostMaterialRequest(

	@field:SerializedName("truck_id")
	val truckId: String,

	@field:SerializedName("driver_id")
	val driverId: String,

	@field:SerializedName("station_id")
	val stationId: String,

	@field:SerializedName("checker_id")
	val checkerId: String,

	@field:SerializedName("observation_ratio_percentage")
	val ratio: Double,

	@field:SerializedName("remarks")
	val remarks: String
)
