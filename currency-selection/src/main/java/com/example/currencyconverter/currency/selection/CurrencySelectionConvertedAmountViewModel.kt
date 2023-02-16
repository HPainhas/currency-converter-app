package com.example.currencyconverter.currency.selection

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CurrencySelectionConvertedAmountViewModel : ViewModel() {
    val convertedAmount = MutableLiveData<Double>()
    val exchangeRate = MutableLiveData<String>()

    fun updateConvertedAmount(amount: Double) {
        this.convertedAmount.value = amount
    }

    fun updateExchangeRate(context: Context, fromSymbol: String, fromRate: Double, toSymbol: String) {
        this.exchangeRate.value = context.getString(
            R.string.currency_selection_converted_amount_exchange,
            fromSymbol,
            fromRate,
            toSymbol
        )
    }
}
