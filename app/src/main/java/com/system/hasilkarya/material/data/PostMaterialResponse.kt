package com.system.hasilkarya.material.data

import com.google.gson.annotations.SerializedName

data class PostMaterialResponse(

	@field:SerializedName("data")
	val data: Data1,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Data1(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("solid_volume_estimate")
	val solidVolumeEstimate: Int,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("observation_ratio")
	val observationRatio: Int,

	@field:SerializedName("solid_ratio")
	val solidRatio: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("truck_capacity")
	val truckCapacity: String,

	@field:SerializedName("formatted_date")
	val formattedDate: Any,

	@field:SerializedName("remarks")
	val remarks: String
)
