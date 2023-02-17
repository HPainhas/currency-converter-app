package com.example.currencyconverter.currency.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrencySelectionAmountViewModel : ViewModel() {

    private var _amount = MutableLiveData(0.00)
    val amount: LiveData<Double> = _amount

    fun updateAmount(newAmount: Double) {
        this._amount.value = newAmount
    }
}