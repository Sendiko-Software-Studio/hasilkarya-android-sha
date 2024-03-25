package com.system.hasilkarya.gas.data

import com.google.gson.annotations.SerializedName

data class TruckGasRequest(

	@field:SerializedName("volume")
	val volume: Any,

	@field:SerializedName("truck_id")
	val truckId: String,

	@field:SerializedName("driver_id")
	val driverId: String,

	@field:SerializedName("odometer")
	val odometer: Any,

	@field:SerializedName("station_id")
	val stationId: String,

	@field:SerializedName("gas_operator_id")
	val gasOperatorId: String
)
