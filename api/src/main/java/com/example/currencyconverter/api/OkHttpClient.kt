package com.example.currencyconverter.api

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpClient {

    companion object {

        fun getRequest(
            context: Context,
            url: String,
            identifier: String,
            callback: ApiResponseCallback
        ) {
            val request = Request.Builder()
                .url(url)
                .cacheControl(
                    CacheControl.Builder()
                        .maxStale(2, TimeUnit.HOURS)
                        .build()
                )
                .get()
                .addHeader("accept", "application/json")
                .build()

            OkHttpClientSingleton.getInstance(context).newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onFailureApiResponse(e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onFailureApiResponse("Response was not successful: {${response.message}}")
                        return
                    }

                    val responseBody = response.body?.string()
                    callback.onSuccessApiResponse(responseBody!!, identifier)
                }
            })
        }

        suspend fun getRequestAsync(context: Context, url: String): JSONObject? {
            val client = OkHttpClientSingleton.getInstance(context)
            val request = Request.Builder()
                .url(url)
                .cacheControl(
                    CacheControl.Builder()
                        .maxStale(2, TimeUnit.HOURS)
                        .build()
                )
                .get()
                .addHeader("accept", "application/json")
                .build()

            return withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) throw IOException("Unsuccessful response -> {$response}")

                val responseBody = response.body?.string()
                responseBody?.let { JSONObject(it) }
            }
        }
    }
}