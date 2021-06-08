package com.rodda.roddaapplication.model.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(

	@field:SerializedName("images")
	val images: List<String>,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("time")
	val time: String
)