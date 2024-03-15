package com.system.hasilkarya.dashboard.data

import com.google.gson.annotations.SerializedName

data class PostMaterialResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("formatted_date")
	val formattedDate: String,

	@field:SerializedName("remarks")
	val remarks: Any
)
