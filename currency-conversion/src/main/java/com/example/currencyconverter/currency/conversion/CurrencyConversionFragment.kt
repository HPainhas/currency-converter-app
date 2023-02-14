package com.example.currencyconverter.currency.conversion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.conversion.databinding.CurrencyConversionFragmentBinding
import com.example.currencyconverter.currency.selection.CurrencySelectionFragment

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

        parentFragmentManager.commit {
            add(
                R.id.currency_conversion_fragment_container_view,
                CurrencySelectionFragment()
            )
        }
    }
}