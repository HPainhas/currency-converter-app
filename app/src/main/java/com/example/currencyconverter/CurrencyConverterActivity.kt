package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.selection.CurrencySelectionConvertedAmountViewModel
import com.example.currencyconverter.currency.selection.CurrencyItemViewModel
import com.example.currencyconverter.currency.selection.CurrencyItemViewModels
import com.example.currencyconverter.currency.selection.CurrencySelectionAmountViewModel
import com.example.currencyconverter.navigationbar.NavigationBarFragment


class CurrencyConverterActivity : AppCompatActivity(R.layout.activity_currency_converter) {

    private val first: CurrencyItemViewModel by viewModels()
    private val second: CurrencyItemViewModel by viewModels()
    private val currencySelectionAmountViewModel: CurrencySelectionAmountViewModel by viewModels()
    private val currencySelectionConvertedAmountViewModel: CurrencySelectionConvertedAmountViewModel by viewModels()

    // TODO - Figure out how to implement Dagger dependency injection into the app to help
    //  pass the viewModels around through @Inject

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        /* disable back button */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(
                    R.id.currency_converter_fragment_container_view,
                    NavigationBarFragment.newInstance(CurrencyItemViewModels(first, second))
                )
            }
        }
    }
}