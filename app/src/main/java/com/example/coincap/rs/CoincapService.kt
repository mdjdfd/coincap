package com.example.coincap.rs

import com.example.coincap.di.IoDispatcher
import com.example.coincap.rp.model.Asset
import com.example.coincap.rp.network.ApiClient
import com.example.coincap.rp.network.Endpoints
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This class is responsible for making api call. Apiclient and CoroutineDispatcher is injected via constructor injection.
 * @param apiClient helps calling REST API
 * @param ioDispatcher perform api call in IO dispatcher.
 *
 */

class CoincapService @Inject constructor(
    private val apiClient: ApiClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getAssets(): Result<List<Asset>> = apiCall(ioDispatcher){
        apiClient.getAssets(Endpoints.LIMIT).data
    }

    suspend fun getAsset(id: String): Result<Asset> = apiCall(ioDispatcher){
        apiClient.getAsset(id).data
    }
}

/**
 * Function that initiate the network call in a catching block and returns success/failure response.
 * @param dispatcher instance of CoroutineDispatcher
 * @param call provided lambda function to call
 * @return returns success/failure response.
 */
suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
): Result<T> = runCatching {
    withContext(dispatcher){
        call.invoke()
    }
}
