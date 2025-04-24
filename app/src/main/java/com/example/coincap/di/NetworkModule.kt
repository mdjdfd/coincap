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

/**
 * Singleton module contains all component classes and dependency for network request.
 * Scope of the components throughout application lifecycle. Scope can be modified to activity, fragment, service
 */

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /**
     * Provides base url from generated build config.
     * @return returns api base url.
     */
    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl() = Endpoints.BASE_URL

    /**
     * Provides HttpLoggingInterceptor based on build variants
     * @return returns logging interceptor instance.
     */
    @Provides
    @Singleton
    @Named("loggingInterceptor")
    fun provideLoggingInterceptor(): Interceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor()
    }

    /**
     * Provides interceptor that contains authorization parameters in header.
     * @return returns auth interceptor instance.
     */
    @Provides
    @Singleton
    @Named("authInterceptor")
    fun provideAuthInterceptor(): Interceptor = AuthInterceptor()


    /**
     * Provides list of OkHttpInterceptor.
     * @param loggingInterceptor instance of logging interceptor.
     * @param authInterceptor instance of auth interceptor.
     * @return returns list of interceptors.
     */
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

    /**
     * Provides OkHttpClient builder with connect, read and write timeout
     * @return returns OkHttpClient.Builder instance
     */
    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
    }

    /**
     * Provides OkHttpClient chaining the interceptors.
     * @param builder OkHttpClient.Builder instance
     * @param interceptors list of interceptors to be added into chain
     * @return returns OkHttpClient instance.
     */
    @Provides
    @Singleton
    @Named("okhttpClient")
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder,
        @Named("interceptors") interceptors: Interceptors
    ): OkHttpClient = builder.apply {
        interceptors.interceptors.forEach { addInterceptor(it) }
    }.build()


    /**
     * Provides Retrofit instance containing baseurl, OkHttpClient and Json to Object converter factory.
     * @param okHttpClient OkHttpClient instance
     * @param baseUrl base url in String
     * @return returns retrofit instance.
     */
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

    /**
     * Provides ApiClient interface created by retrofit.
     * @param retrofit Retrofit instance
     * @return returns ApiClient class.
     */
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)

}

/**
 * Data class responsible for getter and setter of OkHttpInterceptors
 */
data class Interceptors(val interceptors: List<Interceptor>)