package com.example.coincap.di

import com.example.coincap.rp.network.AuthInterceptor
import com.example.coincap.rp.network.ApiClient
import com.example.coincap.rp.network.Endpoints
import com.example.coincap.rp.network.ErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.orbitmvi.orbit.viewmodel.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl() = Endpoints.BASE_URL

    @Provides
    @Singleton
    @Named("loggingInterceptor")
    fun provideLoggingInterceptor(): Interceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor()
    }

    @Provides
    @Singleton
    @Named("authInterceptor")
    fun provideAuthInterceptor(): Interceptor = AuthInterceptor()


    @Provides
    @Singleton
    @Named("interceptors")
    fun provideHttpInterceptors(
        @Named("loggingInterceptor") loggingInterceptor: Interceptor,
        @Named("authInterceptor") authInterceptor: Interceptor
    ): Interceptors {
        val interceptors = listOf(
            ErrorInterceptor(),
            loggingInterceptor,
            authInterceptor
        )
        return Interceptors(interceptors)
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(0L, TimeUnit.SECONDS)
        readTimeout(0L, TimeUnit.SECONDS)
        writeTimeout(0L, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    @Named("okhttpClient")
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder,
        @Named("interceptors") interceptors: Interceptors
    ): OkHttpClient = builder.apply {
        interceptors.interceptors.forEach { addInterceptor(it) }
    }.build()


    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        @Named("okhttpClient") okHttpClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)

}

data class Interceptors(val interceptors: List<Interceptor>)