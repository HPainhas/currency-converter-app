package com.example.currencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.currency.selection.CurrencyExchangeViewModel
import com.example.currencyconverter.currency.selection.CurrencyItemViewModel
import com.example.currencyconverter.navigationbar.NavigationBarFragment


class CurrencyConverterActivity : AppCompatActivity(R.layout.activity_currency_converter) {

    private val currencyExchangeViewModel: CurrencyExchangeViewModel by lazy {
        ViewModelProvider(this)[CurrencyExchangeViewModel::class.java]
    }

    private val currencyItemViewModel: CurrencyItemViewModel by lazy {
        ViewModelProvider(this)[CurrencyItemViewModel::class.java]
    }

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
                    NavigationBarFragment()
                )
            }
        }
    }
}