package com.example.currencyconverter.currency.selection

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class CurrencySelectionItemViewModel : ViewModel() {
    private var debounceTimer: Timer? = null
    private val observers = mutableListOf<() -> Unit>()
    private val handler = Handler(Looper.getMainLooper())

    private var _fromSymbol = MutableLiveData("BRL")
    val fromSymbol: LiveData<String> = _fromSymbol

    private var _fromCountryName = MutableLiveData("Brazil")
    val fromCountryName: LiveData<String> = _fromCountryName

    private var _fromCountryCode = MutableLiveData("BRA")
    val fromCountryCode: LiveData<String> = _fromCountryCode

    private var _fromRate = MutableLiveData(5.91)
    val fromRate: LiveData<Double> = _fromRate

    private var _fromImageName = MutableLiveData("br.png")
    val fromImageName: LiveData<String> = _fromImageName

    private var _toSymbol = MutableLiveData("USD")
    val toSymbol: LiveData<String> = _toSymbol

    private var _toCountryName = MutableLiveData("United States")
    val toCountryName: LiveData<String> = _toCountryName

    private var _toCountryCode = MutableLiveData("USA")
    val toCountryCode: LiveData<String> = _toCountryCode

    private var _toRate = MutableLiveData(1.00)
    val toRate: LiveData<Double> = _toRate

    private var _toImageName = MutableLiveData("us.png")
    val toImageName: LiveData<String> = _toImageName

    val updateLiveData = MediatorLiveData<Unit>()

    init {
        updateLiveData.addSource(fromSymbol) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromCountryName) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromCountryCode) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromRate) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(fromImageName) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toSymbol) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toCountryName) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toCountryCode) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toRate) {
            updateLiveData.value = Unit
        }
        updateLiveData.addSource(toImageName) {
            updateLiveData.value = Unit
        }
    }

    fun updateFromData(
        symbol: String?,
        countryName: String?,
        countryCode: String?,
        rate: Double?,
        imageName: String?
    ) {
        Thread {
            handler.post {
                this._fromSymbol.value = symbol
                this._fromCountryName.value = countryName
                this._fromCountryCode.value = countryCode
                this._fromRate.value = rate
                this._fromImageName.value = imageName
            }
        }.start()
    }

    fun updateToData(
        symbol: String?,
        countryName: String?,
        countryCode: String?,
        rate: Double?,
        imageName: String?
    ) {
        Thread {
            handler.post {
                this._toSymbol.value = symbol
                this._toCountryName.value = countryName
                this._toCountryCode.value = countryCode
                this._toRate.value = rate
                this._toImageName.value = imageName
            }
        }.start()
    }

    fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    fun notifyObservers() {
        debounceTimer?.cancel()
        debounceTimer = Timer()
        debounceTimer?.schedule(object : TimerTask() {
            override fun run() {
                observers.forEach { it.invoke() }
            }
        }, 200)
    }
}