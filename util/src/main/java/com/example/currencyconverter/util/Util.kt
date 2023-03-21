package com.example.currencyconverter.util

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Util {

    companion object {

        fun getCurrentDate(pattern: String): String {
            val formatter = SimpleDateFormat(pattern, Locale.US)
            return formatter.format(Date())
        }

        fun getCurrentDateFromString(pattern: String, date: String): Date {
            val formatter = SimpleDateFormat(pattern, Locale.US)
            return formatter.parse(date)!!
        }

        fun getDateOneYearAgo(pattern: String, date: String): String {
            val formatter = SimpleDateFormat(pattern, Locale.US)
            val formattedDate = formatter.parse(date)
            val calendar = Calendar.getInstance()

            if (formattedDate != null) {
                calendar.time = formattedDate
            }

            calendar.add(Calendar.YEAR, -1)
            return formatter.format(calendar.time)
        }

        fun getDateFromTimestamp(pattern: String, timestamp: Long, timezone: String): String? {
            return try {
                val timeZone = TimeZone.getTimeZone(timezone)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp * 1000 // convert to milliseconds
                calendar.timeZone = timeZone

                val formatter = SimpleDateFormat(pattern, Locale.US)
                formatter.format(calendar.time)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun addParametersToUrl(baseUrl: String, parameters: List<String>): String {
            val urlBuilder = StringBuilder(baseUrl)

            if (parameters.isNotEmpty()) {
                urlBuilder.append("?")

                for ((index, parameter) in parameters.withIndex()) {
                    urlBuilder.append(parameter)

                    if (index < parameters.size - 1) {
                        urlBuilder.append("&")
                    }
                }
            }

            return urlBuilder.toString()
        }

        fun removeDollarSignAndCommas(originalString: String): String =
            originalString.replace(Regex("""[$,\\s]"""), "")

        fun removeDollarSignCommasAndDecimalPoints(originalString: String): String =
            originalString.replace(Regex("""[$,.\\s]"""), "")

        fun removeAllNonNumericCharacters(originalString: String): String =
            originalString.replace("\\D".toRegex(), "")

        fun buildCurrencyList(currencyRates: JSONObject): List<Currency> {
            val currencyList: MutableList<Currency> = mutableListOf()

            for (symbol in currencyRates.keys()) {
                val currency = mapCurrency(symbol)

                if (currency != null) {
                    currency.rate = currencyRates.getDouble(symbol)
                    currencyList.add(currency)
                }
            }

            currencyList.sortBy { it.countryName }

            return currencyList
        }

        private fun mapCurrency(currencySymbol: String): Currency? {
            return when (currencySymbol) {
                "AED" -> Currency("AED", "United Arab Emirates", "AE", 0.0, "ae.svg")
                "AUD" -> Currency("AUD", "Australia", "AU", 0.0, "au.svg")
                "BRL" -> Currency("BRL", "Brazil", "BR", 0.0, "br.svg")
                "CAD" -> Currency("CAD", "Canada", "CA", 0.0, "ca.svg")
                "CHF" -> Currency("CHF", "Switzerland", "CH", 0.0, "ch.svg")
                "CNY" -> Currency("CNY", "China", "CN", 0.0, "cn.svg")
                "EUR" -> Currency("EUR", "European Union", "EU", 0.0, "eu.svg")
                "GBP" -> Currency("GBP", "United Kingdom", "GB", 0.0, "gb.svg")
                "HKD" -> Currency("HKD", "Hong Kong", "HK", 0.0, "hk.svg")
                "INR" -> Currency("INR", "India", "IN", 0.0, "in.svg")
                "JPY" -> Currency("JPY", "Japan", "JP", 0.0, "jp.svg")
                "MXN" -> Currency("MXN", "Mexico", "MX", 0.0, "mx.svg")
                "NOK" -> Currency("NOK", "Norway", "NO", 0.0, "no.svg")
                "NZD" -> Currency("NZD", "New Zealand", "NZ", 0.0, "nz.svg")
                "RUB" -> Currency("RUB", "Russia", "RU", 0.0, "ru.svg")
                "SEK" -> Currency("SEK", "Sweden", "SE", 0.0, "se.svg")
                "SGD" -> Currency("SGD", "Singapore", "SG", 0.0, "sg.svg")
                "USD" -> Currency("USD", "United States", "US", 0.0, "us.svg")
                else -> null
            }
        }

        fun getCountryFlagImage(currencySymbol: String): Int =
            when (currencySymbol) {
                "AED" -> com.example.currencyconverter.brandkit.R.drawable.flag_ae
                "AUD" -> com.example.currencyconverter.brandkit.R.drawable.flag_au
                "BRL" -> com.example.currencyconverter.brandkit.R.drawable.flag_br
                "CAD" -> com.example.currencyconverter.brandkit.R.drawable.flag_ca
                "CHF" -> com.example.currencyconverter.brandkit.R.drawable.flag_ch
                "CNY" -> com.example.currencyconverter.brandkit.R.drawable.flag_cn
                "EUR" -> com.example.currencyconverter.brandkit.R.drawable.flag_eu
                "GBP" -> com.example.currencyconverter.brandkit.R.drawable.flag_gb
                "HKD" -> com.example.currencyconverter.brandkit.R.drawable.flag_hk
                "INR" -> com.example.currencyconverter.brandkit.R.drawable.flag_in
                "JPY" -> com.example.currencyconverter.brandkit.R.drawable.flag_jp
                "MXN" -> com.example.currencyconverter.brandkit.R.drawable.flag_mx
                "NOK" -> com.example.currencyconverter.brandkit.R.drawable.flag_no
                "NZD" -> com.example.currencyconverter.brandkit.R.drawable.flag_nz
                "RUB" -> com.example.currencyconverter.brandkit.R.drawable.flag_ru
                "SEK" -> com.example.currencyconverter.brandkit.R.drawable.flag_se
                "SGD" -> com.example.currencyconverter.brandkit.R.drawable.flag_sg
                "USD" -> com.example.currencyconverter.brandkit.R.drawable.flag_us
                else -> com.example.currencyconverter.brandkit.R.drawable.flag_not_found_placeholder
            }
    }
}
