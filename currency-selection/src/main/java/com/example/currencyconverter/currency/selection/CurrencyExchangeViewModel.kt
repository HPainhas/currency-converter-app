package com.example.currencyconverter.currency.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CurrencyExchangeViewModel : ViewModel() {
    val amount = MutableLiveData<String>()
    val convertedAmount = MutableLiveData<String>()
    val exchangeRate = MutableLiveData<String>()

    fun amount(amount: String) {
        this.amount.value = amount
    }

    fun convertedAmount(amount: String) {
        this.convertedAmount.value = amount
    }

    fun exchangeRate(rate: String) {
        this.exchangeRate.value = rate
    }

    fun updateExchangeRateAndConvertedAmount() {

    }

//    private fun getFormattedExchangeRate(
//        context: Context,
//        currencyItemViewModel: CurrencyItemViewModel
//    ): String = context.getString(
//        R.string.currency_selection_item_exchange_rate_conversion,
//        currencyItemViewModel.symbol,
//        currencyItemViewModel.rate.toString(),
//        currencyItemViewModel.symbol,
//    )
}
