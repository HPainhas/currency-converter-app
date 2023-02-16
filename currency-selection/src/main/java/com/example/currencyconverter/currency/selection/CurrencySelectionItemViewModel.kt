package com.example.currencyconverter.currency.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

data class CurrencyItem(
    val symbol: String,
    val country: String,
    val rate: Double,
    val flagUrl: String,
    val imageName: String
)

data class CurrencyItemViewModels(
    val first: CurrencyItemViewModel,
    val second: CurrencyItemViewModel,
) : Serializable

open class CurrencyItemViewModel : ViewModel() {
    val symbol = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val rate = MutableLiveData<Double>()
    val flagUrl = MutableLiveData<String>()
    val imageName = MutableLiveData<String>()

    fun updateData(symbol: String?, country: String?, rate: Double?, flagUrl: String?, imageName: String?) {
        this.symbol.value = symbol
        this.country.value = country
        this.rate.value = rate
        this.flagUrl.value = flagUrl
        this.imageName.value = imageName
    }
}