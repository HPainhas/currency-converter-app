package com.example.currencyconverter.currency.selection

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CurrencySelectionConvertedAmountViewModel : ViewModel() {
    val convertedAmount = MutableLiveData<String>()
    val exchangeRate = MutableLiveData<String>()

    fun updateConvertedAmount(amount: String) {
        this.convertedAmount.value = amount
    }

    fun updateExchangeRate(rate: String) {
        this.exchangeRate.value = rate
    }

    private fun getFormattedExchangeRate(
        context: Context,
        currencyItemViewModel: CurrencyItemViewModel
    ): String = context.getString(
        R.string.currency_selection_converted_amount_exchange,
        currencyItemViewModel.symbol,
        currencyItemViewModel.rate.toString(),
        currencyItemViewModel.symbol,
    )
}
