package com.example.coincap.rp

import com.example.coincap.rp.model.Asset
import com.example.coincap.rs.CoincapService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CoincapDataRepository @Inject constructor(
    private val coincapService: CoincapService,
) : CoincapRepository {

    override suspend fun getAssets(): Flow<Result<List<Asset>>> = flowOf(coincapService.getAssets())

    override suspend fun getAsset(id: String): Flow<Result<Asset>> = flowOf(coincapService.getAsset(id))
}

