package com.example.currencyconverter.currency.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.selection.databinding.CurrencySelectionFragmentBinding
import com.google.gson.Gson

class CurrencySelectionFragment : Fragment(R.layout.currency_selection_fragment) {

    private lateinit var binding: CurrencySelectionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencySelectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            val viewModelList: List<CurrencyItemViewModel> = readFromAsset()

            binding.currencySelectionSpinnerFirstChoice.adapter =
                activity?.let {
                    CurrencySelectionSpinnerAdapter(it, "1,000", viewModelList, shouldShowExchangeRate = false)
                }

            binding.currencySelectionSpinnerSecondChoice.adapter =
                activity?.let {
                    CurrencySelectionSpinnerAdapter(it, "1,000", viewModelList, shouldShowExchangeRate =  true)
                }
        }
    }

    private fun readFromAsset(): List<CurrencyItemViewModel> {
        val fileName = "currencies_dummies.json"
        val bufferReader = resources.assets.open(fileName).bufferedReader()

        val jsonString = bufferReader.use {
            it.readText()
        }

        val gson = Gson()
        return gson.fromJson(
            jsonString,
            Array<CurrencyItemViewModel>::class.java
        ).toList()
    }
}