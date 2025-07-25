package com.rifftyo.core.data.source.remote.network

import com.rifftyo.core.utils.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking { tokenManager.getToken() }
        val requestBuilder = originalRequest.newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        if (response.code == 401 || response.code == 403) {
            runBlocking {
                tokenManager.deleteToken()
            }

            println("Token expired or invalid. Token cleared.")
        }

        return response
    }
}