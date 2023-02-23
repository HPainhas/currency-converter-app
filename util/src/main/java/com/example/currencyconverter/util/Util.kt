package com.example.currencyconverter.util

class Util {

    companion object {

        fun removeDollarSignAndCommas(originalString: String): String =
            originalString.replace(Regex("""[$,]"""), "")

        fun removeDollarSignCommasAndDecimalPoints(originalString: String): String =
            originalString.replace(Regex("""[$,.]"""), "")

        fun removeAllNonNumericCharacters(originalString: String): String =
            originalString.replace("\\D".toRegex(), "")

        fun mapCurrency(symbol: String): Currency? {
            return when (symbol) {
                "USD" -> Currency("USD", "United States", "US", 0.0, "us.svg")
                "AED" -> Currency("AED", "United Arab Emirates", "AE", 0.0, "ae.svg")
                "EUR" -> Currency("EUR", "European Union", "EU", 0.0, "eu.svg")
                "JPY" -> Currency("JPY", "Japan", "JP", 0.0, "jp.svg")
                "GBP" -> Currency("GBP", "United Kingdom", "GB", 0.0, "gb.svg")
                "AUD" -> Currency("AUD", "Australia", "AU", 0.0, "au.svg")
                "CAD" -> Currency("CAD", "Canada", "CA", 0.0, "ca.svg")
                "CHF" -> Currency("CHF", "Switzerland", "CH", 0.0, "ch.svg")
                "CNY" -> Currency("CNY", "China", "CN", 0.0, "cn.svg")
                "HKD" -> Currency("HKD", "Hong Kong", "HK", 0.0, "hk.svg")
                "NZD" -> Currency("NZD", "New Zealand", "NZ", 0.0, "nz.svg")
                "SEK" -> Currency("SEK", "Sweden", "SE", 0.0, "se.svg")
                "NOK" -> Currency("NOK", "Norway", "NO", 0.0, "no.svg")
                "MXN" -> Currency("MXN", "Mexico", "MX", 0.0, "mx.svg")
                "SGD" -> Currency("SGD", "Singapore", "SG", 0.0, "sg.svg")
                "INR" -> Currency("INR", "India", "IN", 0.0, "in.svg")
                "BRL" -> Currency("BRL", "Brazil", "BR", 0.0, "br.svg")
                "RUB" -> Currency("RUB", "Russia", "RU", 0.0, "ru.svg")
                else -> null
            }
        }
    }
}
