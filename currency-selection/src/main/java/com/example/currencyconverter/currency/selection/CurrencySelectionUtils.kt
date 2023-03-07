package com.example.currencyconverter.currency.selection

import android.view.View
import android.widget.AdapterView
import com.example.currencyconverter.util.Currency

object CurrencySelectionUtils {
    fun initializeCurrencySelectionSpinners(
        currencyList: List<Currency>,
        fromCurrencySelectionSpinner: CurrencySelectionSpinner,
        toCurrencySelectionSpinner: CurrencySelectionSpinner,
        currencySelectionItemViewModel: CurrencySelectionItemViewModel
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
                    updateCurrencySelectionItemViewModel(
                        selectedFromCurrency,
                        isFromCurrency = true,
                        currencySelectionItemViewModel
                    )
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
                    updateCurrencySelectionItemViewModel(
                        selectedToCurrency,
                        isFromCurrency = false,
                        currencySelectionItemViewModel
                    )
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

            updateCurrencySelectionItemViewModel(
                fromCurrency,
                isFromCurrency = true,
                currencySelectionItemViewModel
            )
            updateCurrencySelectionItemViewModel(
                toCurrency,
                isFromCurrency = false,
                currencySelectionItemViewModel
            )
        }
    }

    private fun updateCurrencySelectionItemViewModel(
        item: Currency,
        isFromCurrency: Boolean,
        currencySelectionItemViewModel: CurrencySelectionItemViewModel
    ) {
        if (isFromCurrency) {
            currencySelectionItemViewModel.updateFromData(
                item.symbol,
                item.countryName,
                item.countryCode,
                item.rate,
                item.imageName
            )
        } else {
            currencySelectionItemViewModel.updateToData(
                item.symbol,
                item.countryName,
                item.countryCode,
                item.rate,
                item.imageName
            )
        }
    }
}