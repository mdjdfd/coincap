package com.example.coincap.di

import com.example.coincap.util.EventHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


/**
 * Singleton module contains user event handling component.
 * Scope of the components throughout application lifecycle.
 */
@InstallIn(SingletonComponent::class)
@Module
object EventModule {

    /**
     * Provides event handler instance for user event performed.
     * @param mainDispatcher instance of coroutine main dispatcher
     * @return returns event handler instance.
     */
    @Provides
    @Singleton
    fun provideEventHandler(@MainDispatcher mainDispatcher: CoroutineDispatcher) =
        EventHandler(mainDispatcher)
}