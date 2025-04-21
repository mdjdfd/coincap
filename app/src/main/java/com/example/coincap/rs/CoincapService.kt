package com.example.coincap.rs

import com.example.coincap.di.IoDispatcher
import com.example.coincap.rp.model.Asset
import com.example.coincap.rp.network.ApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoincapService @Inject constructor(
    private val apiClient: ApiClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getAssets(): Result<List<Asset>> = apiCall(ioDispatcher){
        apiClient.getAssets().data
    }

    suspend fun getAsset(id: String): Result<Asset> = apiCall(ioDispatcher){
        apiClient.getAsset(id)
    }
}


suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
): Result<T> = runCatching {
    withContext(dispatcher){
        call.invoke()
    }
}
