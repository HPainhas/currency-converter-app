package com.example.currencyconverter.currency.history.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.currencyconverter.api.ExchangeRatesApiLayerApi
import com.example.currencyconverter.currency.history.chart.databinding.CurrencyHistoryChartFragmentBinding
import org.json.JSONObject

class CurrencyHistoryChartFragment : Fragment(R.layout.currency_history_chart_fragment) {

    private lateinit var binding: CurrencyHistoryChartFragmentBinding
    private lateinit var fromCurrencySelectionSpinner: Spinner
    private lateinit var toCurrencySelectionSpinner: Spinner
    private lateinit var historicalRates: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyHistoryChartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            fromCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerTo

            historicalRates = ExchangeRatesApiLayerApi.getCurrencyHistory(
                context = requireContext(),
                fromCurrencySymbol = "USD",
                toCurrencySymbol = "BRL"
            )
        }
    }
}