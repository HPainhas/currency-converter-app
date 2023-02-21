package com.example.currencyconverter.api

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class OkHttpClient {

    companion object {

        private val gson = Gson()

        suspend fun getRequestAsync(url: String): JSONObject? {
            val client = OkHttpClientSingleton.client
            val request = Request.Builder()
                .url(url)
                .build()

            return withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    JSONObject(responseBody)
                } else {
                    null
                }
            }
        }

        fun getRequest(
            url: String,
            callback: (result: Any?, error: String?) -> Unit
        ) {
            val request = Request.Builder()
                .url(url)
                .build()

            OkHttpClientSingleton.client.newCall(request).enqueue(object : Callback {
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
            url: String,
            body: RequestBody,
            callback: (result: Any?, error: String?) -> Unit
        ) {
            val request = Request.Builder().url(url).post(body).build()

            OkHttpClientSingleton.client.newCall(request).enqueue(object : Callback {
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