package com.system.hasilkarya.dashboard.data

import com.google.gson.annotations.SerializedName

data class PostToLogRequest(

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
