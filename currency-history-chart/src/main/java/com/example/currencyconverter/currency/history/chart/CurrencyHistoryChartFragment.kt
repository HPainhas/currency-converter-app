package com.example.currencyconverter.currency.history.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.currencyconverter.api.ExchangeRatesApiLayerApi
import com.example.currencyconverter.api.OpenExchangeRatesApi
import com.example.currencyconverter.currency.history.chart.databinding.CurrencyHistoryChartFragmentBinding
import com.example.currencyconverter.currency.selection.CurrencySelectionItemViewModel
import com.example.currencyconverter.currency.selection.CurrencySelectionSpinner
import com.example.currencyconverter.util.Currency
import com.example.currencyconverter.util.Util
import org.json.JSONObject

class CurrencyHistoryChartFragment : Fragment(R.layout.currency_history_chart_fragment) {

    private lateinit var binding: CurrencyHistoryChartFragmentBinding
    private lateinit var currencyList: List<Currency>
    private lateinit var historicalRates: JSONObject
    private lateinit var latestExchangeRates: JSONObject
    private lateinit var fromCurrencySelectionSpinner: CurrencySelectionSpinner
    private lateinit var toCurrencySelectionSpinner: CurrencySelectionSpinner

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

        if (savedInstanceState == null) {
            fromCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencyHistoryChartSpinnerTo

            currencySelectionItemViewModel.updateLiveData.observe(viewLifecycleOwner) {
                updateUI()
            }

            latestExchangeRates = OpenExchangeRatesApi.getLatestCurrencyRates(requireContext())
            currencyList = Util.buildCurrencyList(latestExchangeRates.getJSONObject("rates"))

            setUpCurrencySelectionSpinnerAdapters()
            setUpCurrencySelectionSpinnerListeners()
            initializeCurrencySelectionSpinners()

            historicalRates = ExchangeRatesApiLayerApi.getCurrencyHistory(
                context = requireContext(),
                fromCurrencySymbol = "USD",
                toCurrencySymbol = "BRL"
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

        if (currencyList.size > 1) {
            fromCurrencySelectionSpinner.setSelection(0)
            toCurrencySelectionSpinner.setSelection(1)
        }
    }

    private fun setUpCurrencySelectionSpinnerListeners() {
        fromCurrencySelectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedFromCurrency = parent.getItemAtPosition(position) as Currency
                    updateCurrencySelectionItemViewModel(selectedFromCurrency, isFromCurrency = true)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        toCurrencySelectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedToCurrency = parent.getItemAtPosition(position) as Currency
                    updateCurrencySelectionItemViewModel(selectedToCurrency, isFromCurrency = false)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initializeCurrencySelectionSpinners() {
        if (currencyList.size > 1) {
            fromCurrencySelectionSpinner.setSelection(0)
            toCurrencySelectionSpinner.setSelection(1)

            val fromCurrency =
                fromCurrencySelectionSpinner.getItemAtPosition(0) as Currency
            val toCurrency =
                toCurrencySelectionSpinner.getItemAtPosition(1) as Currency

            updateCurrencySelectionItemViewModel(fromCurrency, isFromCurrency = true)
            updateCurrencySelectionItemViewModel(toCurrency, isFromCurrency = false)
        }
    }

    private fun updateCurrencySelectionItemViewModel(
        item: Currency,
        isFromCurrency: Boolean
    ) {
        if (isFromCurrency) {
            currencySelectionItemViewModel.updateFromData(
                item.symbol,
                item.countryName,
                item.countryCode,
                item.rate,
                item.imageName
            )
        } else {
            currencySelectionItemViewModel.updateToData(
                item.symbol,
                item.countryName,
                item.countryCode,
                item.rate,
                item.imageName
            )
        }
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
}