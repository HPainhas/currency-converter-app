package com.example.currencyconverter.api

import android.content.Context
import com.example.currencyconverter.util.Util
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.FileNotFoundException

object ExchangeRatesApiLayerApi {

    private const val EXCHANGE_RATES_API_LAYER_CONFIG_FILE = "exchange-rates-api-layer-config.json"
    private const val EXCHANGE_RATES_API_LAYER_BASE_URL = "https://api.apilayer.com/exchangerates_data"

    fun getCurrencyHistory(
        context: Context,
        fromCurrencySymbol: String,
        toCurrencySymbol: String
    ): JSONObject {
        val endDate = Util.getCurrentDate("yyyy-MM-dd")
        val startDate = Util.getDateOneYearAgo("yyyy-MM-dd", endDate)
        val apiKey = getExchangeRatesApiLayerApiKey(context)
        val params = listOf(
            "apikey=$apiKey",
            "start_date=$startDate",
            "end_date=$endDate",
            "base=$fromCurrencySymbol",
            "symbols=$toCurrencySymbol",
        )

        val url = Util.addParametersToUrl(
            "$EXCHANGE_RATES_API_LAYER_BASE_URL/timeseries",
            params
        )

        return runBlocking {
            OkHttpClient.getRequestAsync(context, url)
        } ?: JSONObject()
    }

    fun getCurrencyStatistics(
        context: Context,
        fromCurrencySymbol: String,
        toCurrencySymbol: String
    ): JSONObject {
        val apiKey = getExchangeRatesApiLayerApiKey(context)
        val params = listOf(
            "apiKey=$apiKey",
            "base=$fromCurrencySymbol",
            "symbols=$toCurrencySymbol",
        )

        val url = Util.addParametersToUrl(
            "$EXCHANGE_RATES_API_LAYER_BASE_URL/fluctuation",
            params
        )

        return runBlocking {
            OkHttpClient.getRequestAsync(context, url)
        } ?: JSONObject()
    }

    private fun getExchangeRatesApiLayerApiKey(context: Context): String? {
        return try {
            val inputStream = context.assets.open(EXCHANGE_RATES_API_LAYER_CONFIG_FILE)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val config = Gson().fromJson(jsonString, Config::class.java)

            config?.apiKey
        } catch (e: Exception) {
            e.printStackTrace()
            throw FileNotFoundException()
        }
    }
}