package com.example.coincap.di

import com.example.coincap.util.EventHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object EventModule {

    @Provides
    @Singleton
    fun provideEventHandler(@MainDispatcher mainDispatcher: CoroutineDispatcher) =
        EventHandler(mainDispatcher)
}