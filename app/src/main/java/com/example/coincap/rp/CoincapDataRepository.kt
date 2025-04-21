package com.example.coincap.rp

import com.example.coincap.rp.model.Asset
import com.example.coincap.rs.CoincapService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoincapDataRepository @Inject constructor(
    private val coincapService: CoincapService,
) : CoincapRepository {

    override suspend fun getAssets(): Flow<Result<List<Asset>>> = flow {
        coincapService.getAssets()
            .onSuccess {
                emit(Result.success(it))
            }
            .onFailure {
                emit(Result.failure(Throwable()))
            }
    }

    override suspend fun getAsset(id: String): Flow<Result<Asset>> = flow {
        coincapService.getAsset(id)
            .onSuccess {
                emit(Result.success(it))
            }
            .onFailure {
                emit(Result.failure(Throwable()))
            }
    }

    companion object{
        private const val TAG = "CoincapDataRepository:"
    }

}

