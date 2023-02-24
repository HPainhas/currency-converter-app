package com.example.currencyconverter.currency.selection

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import com.example.currencyconverter.util.Currency

class CurrencySelectionSpinner(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatSpinner(context, attrs) {

    fun setAdapter(currencyList: List<Currency>) {
        this.adapter = CurrencySelectionSpinnerAdapter(context, currencyList)
    }
}