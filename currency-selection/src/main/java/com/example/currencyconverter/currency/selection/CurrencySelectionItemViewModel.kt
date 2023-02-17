package com.example.currencyconverter.currency.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrencySelectionItemViewModel : ViewModel() {
    private var _fromSymbol = MutableLiveData("BRL")
    val fromSymbol: LiveData<String> = _fromSymbol

    private var _fromCountry = MutableLiveData("Brazil")
    val fromCountry: LiveData<String> = _fromCountry

    private var _fromRate = MutableLiveData(5.91)
    val fromRate: LiveData<Double> = _fromRate

    private var _fromFlagUrl = MutableLiveData("https://flagpedia.net/data/flags/normal/br.png")
    val fromFlagUrl: LiveData<String> = _fromFlagUrl

    private var _fromImageName = MutableLiveData("br.png")
    val fromImageName: LiveData<String> = _fromImageName

    private var _toSymbol = MutableLiveData("USD")
    val toSymbol: LiveData<String> = _toSymbol

    private var _toCountry = MutableLiveData("United States")
    val toCountry: LiveData<String> = _toCountry

    private var _toRate = MutableLiveData(1.00)
    val toRate: LiveData<Double> = _toRate

    private var _toFlagUrl = MutableLiveData("https://flagpedia.net/data/flags/normal/us.png")
    val toFlagUrl: LiveData<String> = _toFlagUrl

    private var _toImageName = MutableLiveData("us.png")
    val toImageName: LiveData<String> = _toImageName

    private var _isLastUpdatedCurrencySelectionItemFrom = MutableLiveData(false)
    val isLastUpdatedCurrencySelectionItemFrom: LiveData<Boolean> = _isLastUpdatedCurrencySelectionItemFrom

    val updateLiveData = MediatorLiveData<Unit>()

    init {
        updateLiveData.addSource(fromSymbol) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromCountry) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromRate) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromFlagUrl) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromImageName) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toSymbol) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toCountry) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toRate) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toFlagUrl) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toImageName) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(isLastUpdatedCurrencySelectionItemFrom) {
            updateLiveData.value = Unit
        }
    }

    fun updateFromData(symbol: String?, country: String?, rate: Double?, flagUrl: String?, imageName: String?, isLastUpdatedCurrencySelectionItemFrom: Boolean) {
        this._fromSymbol.value = symbol
        this._fromCountry.value = country
        this._fromRate.value = rate
        this._fromFlagUrl.value = flagUrl
        this._fromImageName.value = imageName
        this._isLastUpdatedCurrencySelectionItemFrom.value = isLastUpdatedCurrencySelectionItemFrom
    }

    fun updateToData(symbol: String?, country: String?, rate: Double?, flagUrl: String?, imageName: String?, isLastUpdatedCurrencySelectionItemFrom: Boolean) {
        this._toSymbol.value = symbol
        this._toCountry.value = country
        this._toRate.value = rate
        this._toFlagUrl.value = flagUrl
        this._toImageName.value = imageName
        this._isLastUpdatedCurrencySelectionItemFrom.value = isLastUpdatedCurrencySelectionItemFrom
    }
}