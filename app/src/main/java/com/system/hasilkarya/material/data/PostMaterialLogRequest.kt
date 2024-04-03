package com.system.hasilkarya.material.data

import com.google.gson.annotations.SerializedName

data class PostMaterialLogRequest(

	@field:SerializedName("truck_id")
	val truckId: String,

	@field:SerializedName("driver_id")
	val driverId: String,

	@field:SerializedName("error_log")
	val errorLog: String,

	@field:SerializedName("station_id")
	val stationId: String,

	@field:SerializedName("checker_id")
	val checkerId: String
)
