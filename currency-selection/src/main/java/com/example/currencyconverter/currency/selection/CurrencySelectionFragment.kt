package com.example.currencyconverter.currency.selection

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.currencyconverter.api.ApiResponseCallback
import com.example.currencyconverter.api.OpenExchangeRatesApi
import com.example.currencyconverter.currency.selection.CurrencySelectionUtils.initializeCurrencySelectionSpinners
import com.example.currencyconverter.currency.selection.databinding.CurrencySelectionFragmentBinding
import com.example.currencyconverter.util.Util
import com.example.currencyconverter.util.Currency
import com.example.currencyconverter.util.ProgressBarViewModel
import org.json.JSONObject

class CurrencySelectionFragment : Fragment(R.layout.currency_selection_fragment), ApiResponseCallback {

    private lateinit var binding: CurrencySelectionFragmentBinding
    private lateinit var currencyList: List<Currency>
    private lateinit var latestExchangeRates: JSONObject
    private lateinit var fromCurrencySelectionSpinner: CurrencySelectionSpinner
    private lateinit var toCurrencySelectionSpinner: CurrencySelectionSpinner

    private val handler = Handler(Looper.getMainLooper())
    private val progressBarViewModel: ProgressBarViewModel by activityViewModels()
    private val currencySelectionItemViewModel: CurrencySelectionItemViewModel by activityViewModels()
    private val currencySelectionAmountViewModel: CurrencySelectionAmountViewModel by activityViewModels()

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
        progressBarViewModel.setShowProgressBar(true)

        if (savedInstanceState == null) {
            fromCurrencySelectionSpinner =  binding.currencySelectionSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencySelectionSpinnerTo

            currencySelectionItemViewModel.updateLiveData.observe(viewLifecycleOwner) {
                updateUI()
            }

            currencySelectionAmountViewModel.amount.observe(viewLifecycleOwner) {
                updateUI()
            }

            OpenExchangeRatesApi.getLatestCurrencyRates(
                requireContext(),
                LATEST_EXCHANGE_RATES_ID,
                this
            )

            // Always reset amount to $ 0.00
            currencySelectionAmountViewModel.updateAmount(0.00)
        }
    }

    override fun onSuccessApiResponse(responseBody: String, identifier: String) {
        latestExchangeRates = JSONObject(responseBody)

        if (latestExchangeRates.has("rates")) {
            currencyList = Util.buildCurrencyList(latestExchangeRates.getJSONObject("rates"))

            Thread {
                handler.post {
                    setUpLastUpdatedTime()
                    setUpCurrencySelectionSpinnerAdapters()
                }
            }.start()
        } else {
            Log.d(this.javaClass.simpleName, "latestExchangeRates came back empty" )
            throw Exception("latestExchangeRates came back empty")
        }

        progressBarViewModel.setShowProgressBar(false)
    }

    override fun onFailureApiResponse(errorMessage: String) {
        Log.d(this.javaClass.simpleName, "onFailureApiResponse -> $errorMessage" )
        progressBarViewModel.setShowProgressBar(false)
    }

    private fun setUpLastUpdatedTime() {
        val formattedLastUpdatedTimestamp = Util.getDateFromTimestamp(
            pattern = DATE_FORMAT,
            timestamp = latestExchangeRates.getLong("timestamp"),
            timezone = TIMEZONE
        )

        binding.currencySelectionLastUpdated.text = getString(
            R.string.currency_selection_last_updated,
            formattedLastUpdatedTimestamp
        )
    }

    private fun setUpCurrencySelectionSpinnerAdapters() {
        fromCurrencySelectionSpinner.setAdapter(currencyList)
        toCurrencySelectionSpinner.setAdapter(currencyList)

        initializeCurrencySelectionSpinners(
            currencyList,
            fromCurrencySelectionSpinner,
            toCurrencySelectionSpinner,
            ::updateCurrencySelectionItemViewModel
        )
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
        val amount = currencySelectionAmountViewModel.amount.value!!
        val fromSymbol = currencySelectionItemViewModel.fromSymbol.value!!
        val fromRate = currencySelectionItemViewModel.fromRate.value!!
        val toRate = currencySelectionItemViewModel.toRate.value!!
        val toSymbol = currencySelectionItemViewModel.toSymbol.value!!

        val exchangeRate = toRate / fromRate
        val convertedAmount = amount * (toRate / fromRate)

        val formattedExchangeRate = getString(
            R.string.currency_selection_exchange_rate,
            fromSymbol,
            exchangeRate,
            toSymbol
        )

        binding.currencySelectionAmount.text = getString(
            R.string.currency_selection_amount,
            amount
        )
        binding.currencySelectionExchangeRate.text = formattedExchangeRate
        binding.currencySelectionConvertedAmount.text = getString(
            R.string.currency_selection_converted_amount,
            convertedAmount
        )
    }

    companion object {
        private const val TIMEZONE = "America/Phoenix"
        private const val DATE_FORMAT = "dd MMM yyyy HH:mm:SS"
        private const val LATEST_EXCHANGE_RATES_ID = "latestExchangeRates"
    }
}