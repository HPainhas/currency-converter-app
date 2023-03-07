package com.example.currencyconverter.currency.selection

import android.view.View
import android.widget.AdapterView
import com.example.currencyconverter.util.Currency

object CurrencySelectionUtils {
    fun initializeCurrencySelectionSpinners(
        currencyList: List<Currency>,
        fromCurrencySelectionSpinner: CurrencySelectionSpinner,
        toCurrencySelectionSpinner: CurrencySelectionSpinner,
        updateCurrencySelectionItemViewModel: (Currency, Boolean) -> Unit
    ) {
        // Set up item selected listeners for the spinners
        fromCurrencySelectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedFromCurrency = parent.getItemAtPosition(position) as Currency
                    updateCurrencySelectionItemViewModel(selectedFromCurrency, true)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        toCurrencySelectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedToCurrency = parent.getItemAtPosition(position) as Currency
                    updateCurrencySelectionItemViewModel(selectedToCurrency, false)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        // Initialize the spinners and update view model
        if (currencyList.size > 1) {
            fromCurrencySelectionSpinner.setSelection(0)
            toCurrencySelectionSpinner.setSelection(1)

            val fromCurrency =
                fromCurrencySelectionSpinner.getItemAtPosition(0) as Currency
            val toCurrency =
                toCurrencySelectionSpinner.getItemAtPosition(1) as Currency

            updateCurrencySelectionItemViewModel(fromCurrency, true)
            updateCurrencySelectionItemViewModel(toCurrency, false)
        }
    }
}