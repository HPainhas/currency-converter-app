package com.example.currencyconverter.currency.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.currencyconverter.currency.selection.databinding.CurrencySelectionFragmentBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class CurrencySelectionFragment : Fragment(R.layout.currency_selection_fragment) {

    private lateinit var binding: CurrencySelectionFragmentBinding
    private lateinit var fromCurrencySelectionSpinner: Spinner
    private lateinit var toCurrencySelectionSpinner: Spinner
    private lateinit var currencySelectionItemViewModelList: List<CurrencySelectionItem>

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
            currencySelectionItemViewModelList = mapToViewModels(parseJson())
            fromCurrencySelectionSpinner =  binding.currencySelectionSpinnerFrom
            toCurrencySelectionSpinner =  binding.currencySelectionSpinnerTo

            currencySelectionItemViewModel.updateLiveData.observe(viewLifecycleOwner) {
                updateUI()
            }

            currencySelectionAmountViewModel.amount.observe(viewLifecycleOwner) {
                updateUI()
            }

            setUpLastUpdatedTime()
            setUpCurrencySelectionSpinnerAdapters()
            setUpCurrencySelectionSpinnerListeners()
            initializeCurrencySelectionSpinners()
        }
    }

    private fun setUpLastUpdatedTime() {
        val timeZone = TimeZone.getTimeZone(TIMEZONE)
        val calendar = Calendar.getInstance(timeZone)
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        formatter.timeZone = timeZone
        val formattedDateTime = formatter.format(calendar.time)

        binding.currencySelectionLastUpdated.text = getString(
            R.string.currency_selection_last_updated,
            formattedDateTime
        )
    }

    private fun setUpCurrencySelectionSpinnerAdapters() {
        binding.currencySelectionSpinnerFrom.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it, currencySelectionItemViewModelList)
            }

        binding.currencySelectionSpinnerTo.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it,  currencySelectionItemViewModelList)
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
                    val selectedFromCurrency = parent.getItemAtPosition(position) as CurrencySelectionItem
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
                    val selectedToCurrency = parent.getItemAtPosition(position) as CurrencySelectionItem
                    updateCurrencySelectionItemViewModel(selectedToCurrency, isFromData = false)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun initializeCurrencySelectionSpinners() {
        if (currencySelectionItemViewModelList.size > 1) {
            fromCurrencySelectionSpinner.setSelection(0)
            toCurrencySelectionSpinner.setSelection(1)

            val fromCurrency =
                fromCurrencySelectionSpinner.getItemAtPosition(0) as CurrencySelectionItem
            val toCurrency =
                toCurrencySelectionSpinner.getItemAtPosition(1) as CurrencySelectionItem

            updateCurrencySelectionItemViewModel(fromCurrency, isFromData = true)
            updateCurrencySelectionItemViewModel(toCurrency, isFromData = false)
        }
    }

    private fun updateCurrencySelectionItemViewModel(
        item: CurrencySelectionItem,
        isFromData: Boolean
    ) {
        if (isFromData) {
            currencySelectionItemViewModel.updateFromData(
                item.symbol,
                item.country,
                item.rate,
                item.flagUrl,
                item.imageName,
                isLastUpdatedCurrencySelectionItemFrom = true
            )
        } else {
            currencySelectionItemViewModel.updateToData(
                item.symbol,
                item.country,
                item.rate,
                item.flagUrl,
                item.imageName,
                isLastUpdatedCurrencySelectionItemFrom = false
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

        val convertedAmount = amount * (toRate / fromRate)

        binding.currencySelectionConvertedAmount.text = getString(
            R.string.currency_selection_converted_amount,
            convertedAmount
        )
    }

    private fun parseJson(): List<CurrencySelectionItem> {
        val listType = object : TypeToken<List<CurrencySelectionItem>>() {}.type
        val bufferReader = resources.assets.open(FILENAME).bufferedReader()
        val jsonString = bufferReader.use { it.readText() }

        return Gson().fromJson(jsonString, listType)
    }

    private fun mapToViewModels(
        currencySelectionItems: List<CurrencySelectionItem>
    ): List<CurrencySelectionItem> {
        return currencySelectionItems.map { item ->
            CurrencySelectionItem(
                item.symbol,
                item.country,
                item.rate,
                item.flagUrl,
                item.imageName
            )
        }
    }

    companion object {
        private const val FILENAME = "currencies_dummies.json"
        private const val TIMEZONE = "America/Phoenix"
        private const val DATE_FORMAT = "dd MMM yyyy HH:mm"
    }
}