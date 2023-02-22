package com.example.currencyconverter.util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class Util {

    companion object {

        fun logDebug(message: String) {
            Log.d("HENRIQUE", message)
        }

        fun loadImage(
            imageView: ImageView,
            imageUrl: String,
            progressDrawable: CircularProgressDrawable
        ) {
            val options: RequestOptions = RequestOptions()
                .placeholder(progressDrawable)
                .error(com.example.currencyconverter.brandkit.R.drawable.ic_error_outline_40)

            Glide.with(imageView.context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(imageView)
        }

        fun getProgressDrawable(context: Context): CircularProgressDrawable {
            val progressDrawable = CircularProgressDrawable(context)
            progressDrawable.strokeWidth = 10f
            progressDrawable.centerRadius = 50f
            progressDrawable.start()
            return progressDrawable
        }

        fun removeDollarSignAndCommas(originalString: String): String =
            originalString.replace(Regex("""[$,]"""), "")

        fun removeDollarSignCommasAndDecimalPoints(originalString: String): String =
            originalString.replace(Regex("""[$,.]"""), "")

        fun removeAllNonNumericCharacters(originalString: String): String =
            originalString.replace("\\D".toRegex(), "")

        fun mapCurrency(symbol: String): Currency? {
            return when (symbol) {
                "USD" -> Currency("USD", "United States", "US", 0.0, "us.png")
                "EUR" -> Currency("EUR", "European Union", "EU", 0.0, "eu.png")
                "JPY" -> Currency("JPY", "Japan", "JP", 0.0, "jp.png")
                "GBP" -> Currency("GBP", "United Kingdom", "GB", 0.0, "gb.png")
                "AUD" -> Currency("AUD", "Australia", "AU", 0.0, "au.png")
                "CAD" -> Currency("CAD", "Canada", "CA", 0.0, "ca.png")
                "CHF" -> Currency("CHF", "Switzerland", "CH", 0.0, "ch.png")
                "CNY" -> Currency("CNY", "China", "CN", 0.0, "cn.png")
                "HKD" -> Currency("HKD", "Hong Kong", "HK", 0.0, "hk.png")
                "NZD" -> Currency("NZD", "New Zealand", "NZ", 0.0, "nz.png")
                "SEK" -> Currency("SEK", "Sweden", "SE", 0.0, "se.png")
                "NOK" -> Currency("NOK", "Norway", "NO", 0.0, "no.png")
                "MXN" -> Currency("MXN", "Mexico", "MX", 0.0, "mx.png")
                "SGD" -> Currency("SGD", "Singapore", "SG", 0.0, "sg.png")
                "INR" -> Currency("INR", "India", "IN", 0.0, "in.png")
                "BRL" -> Currency("BRL", "Brazil", "BR", 0.0, "br.png")
                "RUB" -> Currency("RUB", "Russia", "RU", 0.0, "ru.png")
                else -> null
            }
        }
    }
}