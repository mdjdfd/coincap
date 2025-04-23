package com.example.coincap.rp

import com.example.coincap.rp.model.Asset
import kotlinx.coroutines.flow.Flow

/**
 * Interface to help providing api service.
 */

interface CoincapRepository {
    suspend fun getAssets(): Flow<Result<List<Asset>>>
    suspend fun getAsset(id: String): Flow<Result<Asset>>
}