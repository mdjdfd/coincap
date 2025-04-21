package com.example.coincap.rp.network

import com.example.coincap.rp.model.AssetListModel
import com.example.coincap.rp.model.SingleAssetModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("/v3/assets")
    suspend fun getAssets(@Query("limit") limit: String): AssetListModel

    @GET("/v3/assets/{id}")
    suspend fun getAsset(@Path("id") id: String): SingleAssetModel
}