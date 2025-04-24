package com.example.coincap.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Module that provides different types of coroutine dispatcher.
 */
@InstallIn(SingletonComponent::class)
@Module
object CoroutineScopeModule {

    /**
     * Provides coroutine default dispatcher.
     */
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides coroutine IO dispatcher.
     */
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides coroutine main dispatcher.
     */
    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    /**
     * Provides coroutine main/immediate dispatcher.
     */
    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    /**
     *  Provides application-scoped CoroutineScope
     */
    @Singleton
    @Provides
    fun providesCoroutineScope(@DefaultDispatcher defaultDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + defaultDispatcher)
    }
}