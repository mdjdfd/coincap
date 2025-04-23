package com.example.coincap.rp.network

import com.example.coincap.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * AuthInterceptor is responsible to chain the OkHttp interceptor for adding necessary auth header to http request.
 */

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val mainRequest = chain.request()

        val requestBuilder = mainRequest.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    companion object{
        private const val TAG = "AuthInterceptor:"
    }
}