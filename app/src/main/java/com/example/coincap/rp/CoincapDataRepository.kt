package com.example.coincap.rp

import com.example.coincap.rp.model.Asset
import com.example.coincap.rs.CoincapService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * This class is responsible for collecting flow of data from the service. Api service is injected via constructor injection.
 * @param coincapService service class where the api call is made.
 *
 */

class CoincapDataRepository @Inject constructor(
    private val coincapService: CoincapService,
) : CoincapRepository {

    override suspend fun getAssets(): Flow<Result<List<Asset>>> = flowOf(coincapService.getAssets())

    override suspend fun getAsset(id: String): Flow<Result<Asset>> = flowOf(coincapService.getAsset(id))
}

