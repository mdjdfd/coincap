package com.example.coincap.rp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ErrorInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            400 -> {
                Log.e(TAG, "Bad Request Error")
            }
            401 -> {
                Log.e(TAG, "Unauthorized Error")
            }
            403 -> {
                Log.e(TAG, "Forbidden")
            }
            404 -> {
                Log.e(TAG, "Not Found")
            }
        }
        return response
    }

    companion object{
        private const val TAG = "ErrorInterceptor:"
    }
}