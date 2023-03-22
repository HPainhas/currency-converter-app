package com.example.currencyconverter.currency.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.stats.R
import com.example.currencyconverter.currency.stats.databinding.CurrencyStatisticsFragmentBinding
import org.json.JSONObject

class CurrencyStatisticsFragment : Fragment(R.layout.currency_statistics_fragment) {

    private lateinit var binding: CurrencyStatisticsFragmentBinding

    private val fluctuationRates by lazy {
        JSONObject(requireArguments().getString(ARG_FLUCTUATION_RATES)!!)
    }

    private val toCurrencySymbol by lazy {
        requireArguments().getString(ARG_TO_CURRENCY_SYMBOL)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyStatisticsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setUpCurrencyStatistics()
        }
    }

    private fun setUpCurrencyStatistics() {
        var high = 0.0
        var low = 0.0
        var changePercentage = 0.0

        if (fluctuationRates.getBoolean("success")) {
            val fluctuationRateJsonObject = fluctuationRates
                .getJSONObject("rates")
                .getJSONObject(toCurrencySymbol)

            high = fluctuationRateJsonObject.getDouble("start_rate")
            low = fluctuationRateJsonObject.getDouble("end_rate")
            changePercentage = fluctuationRateJsonObject.getDouble("change_pct") * 100
        }

        binding.currencyStatisticsHigh.text = if (high == 0.0) "N/A" else {
            getString(R.string.currency_statistics_high_rate, high)
        }
        binding.currencyStatisticsLow.text = if (low == 0.0) "N/A" else {
            getString(R.string.currency_statistics_low_rate, low)
        }
        binding.currencyStatisticsChangePercentage.text = if (changePercentage == 0.0) "N/A" else {
            getString(R.string.currency_statistics_change_percentage_rate, changePercentage)
        }
    }

    companion object {

        private const val ARG_FLUCTUATION_RATES = "CurrencyStatisticsFragment.ARG_FLUCTUATION_RATES"
        private const val ARG_TO_CURRENCY_SYMBOL = "CurrencyStatisticsFragment.ARG_TO_CURRENCY_SYMBOL"

        fun newInstance(
            fluctuationRates: String,
            toCurrencySymbol: String
        ) : CurrencyStatisticsFragment =
            CurrencyStatisticsFragment().apply {
                this.arguments = Bundle().apply {
                    putString(ARG_FLUCTUATION_RATES, fluctuationRates)
                    putString(ARG_TO_CURRENCY_SYMBOL, toCurrencySymbol)
                }
            }
    }

}