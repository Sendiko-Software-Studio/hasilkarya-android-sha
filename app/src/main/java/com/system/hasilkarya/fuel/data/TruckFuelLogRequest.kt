package com.system.hasilkarya.fuel.data

import com.google.gson.annotations.SerializedName

data class TruckFuelLogRequest(

	@field:SerializedName("volume")
	val volume: Any,

	@field:SerializedName("truck_id")
	val truckId: String,

	@field:SerializedName("driver_id")
	val driverId: String,

	@field:SerializedName("odometer")
	val odometer: Any,

	@field:SerializedName("error_log")
	val errorLog: String,

	@field:SerializedName("station_id")
	val stationId: String,

	@field:SerializedName("gas_operator_id")
	val gasOperatorId: String,

	@field:SerializedName("remarks")
	val remarks: String
)
