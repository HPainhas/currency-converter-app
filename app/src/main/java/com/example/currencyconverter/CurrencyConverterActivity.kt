package com.example.currencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.currencyconverter.navigationbar.NavigationBarFragment


class CurrencyConverterActivity : AppCompatActivity(R.layout.activity_currency_converter) {

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