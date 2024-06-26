package com.example.currencyconverter.currency.history.chart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.currencyconverter.api.ApiResponseCallback
import com.example.currencyconverter.api.ExchangeRatesApiLayerApi
import com.example.currencyconverter.api.OpenExchangeRatesApi
import com.example.currencyconverter.currency.history.chart.databinding.CurrencyHistoryChartFragmentBinding
import com.example.currencyconverter.currency.selection.CurrencySelectionItemViewModel
import com.example.currencyconverter.currency.selection.CurrencySelectionSpinner
import com.example.currencyconverter.currency.selection.CurrencySelectionUtils
import com.example.currencyconverter.currency.statistics.CurrencyStatisticsFragment
import com.example.currencyconverter.util.Currency
import com.example.currencyconverter.util.ProgressBarViewModel
import com.example.currencyconverter.util.Util
import org.json.JSONObject

class CurrencyHistoryChartFragment : Fragment(R.layout.currency_history_chart_fragment), ApiResponseCallback {

    private lateinit var binding: CurrencyHistoryChartFragmentBinding
    private lateinit var currencyList: List<Currency>
    private lateinit var historicalRates: JSONObject
    private lateinit var fluctuationRates: JSONObject
    private lateinit var latestExchangeRates: JSONObject
    private lateinit var fromCurrencySelectionSpinner: CurrencySelectionSpinner
    private lateinit var toCurrencySelectionSpinner: CurrencySelectionSpinner

    private val handler = Handler(Looper.getMainLooper())
    private val progressBarViewModel: ProgressBarViewModel by activityViewModels()
    private val currencySelectionItemViewModel: CurrencySelectionItemViewModel by activityViewModels()

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
        progressBarViewModel.setShowProgressBar(true)

        if (savedInstanceState == null) {
            fromCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerTo

            OpenExchangeRatesApi.getLatestCurrencyRates(
                requireContext(),
                LATEST_EXCHANGE_RATES_ID,
                this
            )

            currencySelectionItemViewModel.addObserver {
                progressBarViewModel.setShowProgressBar(true)

                ExchangeRatesApiLayerApi.getCurrencyStatistics(
                    context = requireContext(),
                    fromCurrencySymbol = currencySelectionItemViewModel.fromSymbol.value!!,
                    toCurrencySymbol = currencySelectionItemViewModel.toSymbol.value!!,
                    FLUCTUATION_RATES_ID,
                    this@CurrencyHistoryChartFragment
                )

                ExchangeRatesApiLayerApi.getCurrencyHistory(
                    context = requireContext(),
                    fromCurrencySymbol = currencySelectionItemViewModel.fromSymbol.value!!,
                    toCurrencySymbol = currencySelectionItemViewModel.toSymbol.value!!,
                    HISTORICAL_RATES_ID,
                    this@CurrencyHistoryChartFragment
                )
            }

            currencySelectionItemViewModel.updateLiveData.observe(viewLifecycleOwner) {
                currencySelectionItemViewModel.notifyObservers()
            }
        }
    }

    override fun onSuccessApiResponse(responseBody: String, identifier: String) {
        // TODO - try create just one JSONObject called responseJson
        when(identifier) {
            LATEST_EXCHANGE_RATES_ID -> {
                latestExchangeRates = JSONObject(responseBody)

                if (latestExchangeRates.has("rates")) {
                    currencyList = Util.buildCurrencyList(latestExchangeRates.getJSONObject("rates"))

                    Thread {
                        handler.post { setUpCurrencySelectionSpinnerAdapters() }
                    }.start()
                } else {
                    Log.d(this.javaClass.simpleName, "latestExchangeRates came back empty" )
                    throw Exception("latestExchangeRates came back empty")
                }
            }
            FLUCTUATION_RATES_ID -> {
                fluctuationRates = JSONObject(responseBody)
                loadCurrencyStatisticsFragment()
                handler.post { updateUI() }
            }
            HISTORICAL_RATES_ID -> {
                historicalRates = JSONObject(responseBody)
                loadCurrencyChartFragment()
                handler.post { updateUI() }
            }
        }

        progressBarViewModel.setShowProgressBar(false)
    }

    override fun onFailureApiResponse(errorMessage: String) {
        handler.post { updateUI() }
        Log.d(this.javaClass.simpleName, "onFailureApiResponse -> $errorMessage" )
        progressBarViewModel.setShowProgressBar(false)
    }

    private fun loadCurrencyChartFragment() {
        parentFragmentManager.commit {
            replace(
                R.id.currency_history_chart_container,
                CurrencyChartFragment.newInstance(
                    historicalRates.toString(),
                    currencySelectionItemViewModel.toSymbol.value.toString()
                )
            )
        }
    }

    private fun loadCurrencyStatisticsFragment() {
        parentFragmentManager.commit {
            replace(
                R.id.currency_history_chart_statistics_container,
                CurrencyStatisticsFragment.newInstance(
                    fluctuationRates.toString(),
                    currencySelectionItemViewModel.toSymbol.value.toString()
                )
            )
        }
    }

    private fun setUpCurrencySelectionSpinnerAdapters() {
        fromCurrencySelectionSpinner.setAdapter(
            currencyList,
            isCountryNameVisible = false,
            includeParenthesis = false
        )

        toCurrencySelectionSpinner.setAdapter(
            currencyList,
            isCountryNameVisible = false,
            includeParenthesis = false
        )

        CurrencySelectionUtils.initializeCurrencySelectionSpinners(
            currencyList,
            fromCurrencySelectionSpinner,
            toCurrencySelectionSpinner,
            currencySelectionItemViewModel
        )
    }

    private fun updateUI() {
        // All of the fields in the view models have a default value, so we
        // can assert non-null here with !!
        val fromSymbol = currencySelectionItemViewModel.fromSymbol.value!!
        val fromRate = currencySelectionItemViewModel.fromRate.value!!
        val toRate = currencySelectionItemViewModel.toRate.value!!
        val toSymbol = currencySelectionItemViewModel.toSymbol.value!!

        val exchangeRate = toRate / fromRate

        val formattedExchangeRate = getString(
            R.string.currency_history_chart_exchange_rate,
            fromSymbol,
            exchangeRate,
            toSymbol
        )

        binding.currencyHistoryChartExchangeRate.text = formattedExchangeRate
    }

    companion object {
        private const val HISTORICAL_RATES_ID = "historicalRates"
        private const val FLUCTUATION_RATES_ID = "fluctuationRates"
        private const val LATEST_EXCHANGE_RATES_ID = "latestExchangeRates"
    }
}