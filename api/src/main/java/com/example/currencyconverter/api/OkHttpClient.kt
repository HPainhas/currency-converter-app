package com.example.currencyconverter.api

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpClient {

    companion object {

        private val gson = Gson()

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

        fun getRequest(
            context: Context,
            url: String,
            callback: (result: Any?, error: String?) -> Unit
        ) {
            val request = Request.Builder()
                .url(url)
                .cacheControl(
                    CacheControl.Builder()
                        .maxStale(2, TimeUnit.HOURS)
                        .onlyIfCached()
                        .build()
                )
                .get()
                .addHeader("accept", "application/json")
                .build()

            OkHttpClientSingleton.getInstance(context).newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val result = gson.fromJson(responseBody, Any::class.java)
                        callback(result, null)
                    } else {
                        callback(null, "Error: ${response.code} ${response.message}")
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e.message)
                }
            })
        }

        fun postRequest(
            context: Context,
            url: String,
            body: RequestBody,
            callback: (result: Any?, error: String?) -> Unit
        ) {
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            OkHttpClientSingleton.getInstance(context).newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val result = gson.fromJson(responseBody, Any::class.java)
                        callback(result, null)
                    } else {
                        callback(null, "Error: ${response.code} ${response.message}")
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e.message)
                }
            })
        }
    }
}