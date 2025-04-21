package com.example.coincap.rp.network

import com.example.coincap.rp.model.Asset
import com.example.coincap.rp.model.AssetModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {

    @GET("/v3/assets")
    suspend fun getAssets(): AssetModel

    @GET("/v3/assets/{id}")
    suspend fun getAsset(@Path("id") id: String): Asset
}