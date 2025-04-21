package com.example.coincap.rp.model

import com.google.gson.annotations.SerializedName

data class AssetModel(
	@field:SerializedName("data") val data: List<Asset>,
	@field:SerializedName("timestamp") val timestamp: Long
)

