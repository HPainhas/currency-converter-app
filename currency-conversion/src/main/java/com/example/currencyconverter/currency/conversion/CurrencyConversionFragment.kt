package com.example.currencyconverter.currency.conversion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.conversion.databinding.CurrencyConversionFragmentBinding

class CurrencyConversionFragment : Fragment(R.layout.currency_conversion_fragment) {

    private lateinit var binding: CurrencyConversionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyConversionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HENRIQUE", "CurrencyConversionFragment displayed")
    }
}