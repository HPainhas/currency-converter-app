package com.example.currencyconverter.currency.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.currencyconverter.api.OpenExchangeRatesApi
import com.example.currencyconverter.currency.selection.databinding.CurrencySelectionFragmentBinding
import com.example.currencyconverter.util.Util
import com.example.currencyconverter.util.Currency
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CurrencySelectionFragment : Fragment(R.layout.currency_selection_fragment) {

    private lateinit var binding: CurrencySelectionFragmentBinding
    private lateinit var fromCurrencySelectionSpinner: Spinner
    private lateinit var toCurrencySelectionSpinner: Spinner
    private lateinit var latestExchangeRates: JSONObject
    private lateinit var currencyList: List<Currency>

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

        if (savedInstanceState == null) {
            fromCurrencySelectionSpinner =  binding.currencySelectionSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencySelectionSpinnerTo

            currencySelectionItemViewModel.updateLiveData.observe(viewLifecycleOwner) {
                updateUI()
            }

            currencySelectionAmountViewModel.amount.observe(viewLifecycleOwner) {
                updateUI()
            }

            latestExchangeRates = OpenExchangeRatesApi.getLatestCurrencyRates(requireContext())

            currencyList = buildCurrencyList()

            setUpLastUpdatedTime()
            setUpCurrencySelectionSpinnerAdapters()
            setUpCurrencySelectionSpinnerListeners()
            initializeCurrencySelectionSpinners()
        }
    }

    private fun setUpLastUpdatedTime() {
        try {
            val lastUpdatedTimestamp = latestExchangeRates.getLong("timestamp")
            val timeZone = TimeZone.getTimeZone(TIMEZONE)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = lastUpdatedTimestamp * 1000 // convert to milliseconds
            calendar.timeZone = timeZone

            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val formattedDateTime = formatter.format(calendar.time)

            binding.currencySelectionLastUpdated.text = getString(
                R.string.currency_selection_last_updated,
                formattedDateTime
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpCurrencySelectionSpinnerAdapters() {
        binding.currencySelectionSpinnerFrom.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it, currencyList)
            }

        binding.currencySelectionSpinnerTo.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it,  currencyList)
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
                    updateCurrencySelectionItemViewModel(selectedFromCurrency, isFromData = true)
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
                    updateCurrencySelectionItemViewModel(selectedToCurrency, isFromData = false)
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

            updateCurrencySelectionItemViewModel(fromCurrency, isFromData = true)
            updateCurrencySelectionItemViewModel(toCurrency, isFromData = false)
        }
    }

    private fun updateCurrencySelectionItemViewModel(
        item: Currency,
        isFromData: Boolean
    ) {
        if (isFromData) {
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
            R.string.currency_selection_converted_amount_exchange,
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

    private fun buildCurrencyList(): List<Currency> {
        val currencyList: MutableList<Currency> = mutableListOf()
        val currencyRates = latestExchangeRates.getJSONObject("rates")

        for (symbol in currencyRates.keys()) {
            val currency = Util.mapCurrency(symbol)

            if (currency != null) {
                currency.rate = currencyRates.getDouble(symbol)
                currencyList.add(currency)
            }
        }

        return currencyList
    }

    companion object {
        private const val TIMEZONE = "America/Phoenix"
        private const val DATE_FORMAT = "dd MMM yyyy HH:mm:SS"
    }
}