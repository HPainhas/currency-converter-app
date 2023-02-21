package com.example.currencyconverter.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientSingleton {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}