package com.renansantiago.restaurantmenu.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RemoteRequestInterceptor(private val apiKey: String) : Interceptor {

    companion object {
        private const val API_KEY = "key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter(API_KEY, apiKey)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}