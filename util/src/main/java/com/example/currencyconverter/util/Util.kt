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
    }
}