package com.example.currencyconverter.api

import android.content.Context
import com.example.currencyconverter.util.Util
import com.google.gson.Gson
import java.io.FileNotFoundException

object ExchangeRatesApiLayerApi {

    private const val EXCHANGE_RATES_API_LAYER_CONFIG_FILE = "exchange-rates-api-layer-config.json"
    private const val EXCHANGE_RATES_API_LAYER_BASE_URL = "https://api.apilayer.com/exchangerates_data"

    fun getCurrencyHistory(
        context: Context,
        fromCurrencySymbol: String,
        toCurrencySymbol: String,
        identifier: String,
        callback: ApiResponseCallback
    ) {
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

        OkHttpClient.getRequest(context, url, identifier, object : ApiResponseCallback {
            override fun onFailureApiResponse(errorMessage: String) {
                callback.onFailureApiResponse(errorMessage)
            }

            override fun onSuccessApiResponse(responseBody: String, identifier: String) {
                callback.onSuccessApiResponse(responseBody, identifier)
            }
        })
    }

    fun getCurrencyStatistics(
        context: Context,
        fromCurrencySymbol: String,
        toCurrencySymbol: String,
        identifier: String,
        callback: ApiResponseCallback
    ) {
        val apiKey = getExchangeRatesApiLayerApiKey(context)
        val params = listOf(
            "apikey=$apiKey",
            "base=$fromCurrencySymbol",
            "symbols=$toCurrencySymbol",
        )

        val url = Util.addParametersToUrl(
            "$EXCHANGE_RATES_API_LAYER_BASE_URL/fluctuation",
            params
        )

        OkHttpClient.getRequest(context, url, identifier, object : ApiResponseCallback {
            override fun onFailureApiResponse(errorMessage: String) {
                callback.onFailureApiResponse(errorMessage)
            }

            override fun onSuccessApiResponse(responseBody: String, identifier: String) {
                callback.onSuccessApiResponse(responseBody, identifier)
            }
        })
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