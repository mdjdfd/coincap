package com.example.coincap.di

import com.example.coincap.rp.CoincapDataRepository
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.rp.network.ApiClient
import com.example.coincap.rs.CoincapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCoincapService(
        apiClient: ApiClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoincapService {
        return CoincapService(apiClient, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideCoincapRepository(
        coincapService: CoincapService,
    ): CoincapRepository {
        return CoincapDataRepository(coincapService)
    }
}