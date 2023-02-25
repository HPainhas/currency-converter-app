package com.example.currencyconverter.currency.selection

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import com.example.currencyconverter.util.Currency

class CurrencySelectionSpinner(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatSpinner(context, attrs) {

    private val countryTextSizeDimenId = R.dimen.currency_selection_spinner_item_country_text_size
    private val countryTextSizeDimenValue = resources.getDimension(countryTextSizeDimenId)
    private val defaultCountryTextSize = countryTextSizeDimenValue /resources.displayMetrics.density

    fun setAdapter(
        currencyList: List<Currency>,
        countryNameTextColor: Int = Color.GRAY,
        currencySymbolTextColor: Int = Color.GRAY,
        countryNameTextSize: Float = defaultCountryTextSize,
        currencySymbolTextSize: Float = defaultCountryTextSize,
        isDropdownIconVisible: Boolean = true
    ) {
        this.adapter = CurrencySelectionSpinnerAdapter(
            context,
            currencyList,
            countryNameTextColor,
            currencySymbolTextColor,
            countryNameTextSize,
            currencySymbolTextSize,
            isDropdownIconVisible
        )
    }
}