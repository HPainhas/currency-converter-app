package com.example.currencyconverter.currency.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrencySelectionAmountViewModel : ViewModel() {

    val amount = MutableLiveData<String>()

    fun updateAmount(amount: String) {
        this.amount.value = amount
    }
}