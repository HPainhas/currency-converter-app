package com.example.currencyconverter.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressBarViewModel: ViewModel() {
    private var _showProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    fun setShowProgressBar(show: Boolean) {
        this._showProgressBar.postValue(show)
    }
}