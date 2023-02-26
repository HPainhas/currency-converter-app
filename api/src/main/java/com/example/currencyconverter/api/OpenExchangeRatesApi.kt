package com.example.currencyconverter.api

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.FileNotFoundException

object OpenExchangeRatesApi {

    private const val OPEN_EXCHANGE_RATES_CONFIG_FILE = "open-exchange-rates-config.json"
    private const val OPEN_EXCHANGE_RATES_BASE_URL = "https://openexchangerates.org/api"

    fun getLatestCurrencyRates(context: Context): JSONObject {
        val appId = getOpenExchangeRatesApiAppId(context)

        return runBlocking {
            OkHttpClient.getRequestAsync(
                context,
                "$OPEN_EXCHANGE_RATES_BASE_URL/latest.json?app_id=$appId"
            )
        } ?: JSONObject()
    }

    private fun getOpenExchangeRatesApiAppId(context: Context): String? {
        return try {
            val inputStream = context.assets.open(OPEN_EXCHANGE_RATES_CONFIG_FILE)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val config = Gson().fromJson(jsonString, Config::class.java)

            config?.appId
        } catch (e: Exception) {
            e.printStackTrace()
            throw FileNotFoundException()
        }
    }
}