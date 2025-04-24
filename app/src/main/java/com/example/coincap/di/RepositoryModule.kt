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

/**
 * Singleton module contains all component classes and dependency for data.
 * Scope of the components throughout application lifecycle.
 */
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    /**
     * Provides service class where api call is being done.
     * @param apiClient instance of api client.
     * @param ioDispatcher instance of coroutine IO dispatcher.
     * @return returns service instance.
     */
    @Singleton
    @Provides
    fun provideCoincapService(
        apiClient: ApiClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoincapService {
        return CoincapService(apiClient, ioDispatcher)
    }

    /**
     * Provides data for the view layer.
     * @param coincapService instance of service class.
     * @return returns the data repository.
     */
    @Singleton
    @Provides
    fun provideCoincapRepository(
        coincapService: CoincapService,
    ): CoincapRepository {
        return CoincapDataRepository(coincapService)
    }
}