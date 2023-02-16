package com.example.currencyconverter.currency.selection

import android.os.Bundle
import android.util.Log
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

class CurrencySelectionFragment : Fragment(R.layout.currency_selection_fragment) {

    private lateinit var binding: CurrencySelectionFragmentBinding
    private lateinit var currencyItemViewModels: CurrencyItemViewModels
    private lateinit var fromCurrencySelectionSpinner: Spinner
    private lateinit var toCurrencySelectionSpinner: Spinner
    private lateinit var currencyItemViewModelList: List<CurrencyItemViewModel>

    private val currencySelectionAmountViewModel: CurrencySelectionAmountViewModel by activityViewModels()
    private val currencySelectionConvertedAmountViewModel: CurrencySelectionConvertedAmountViewModel by activityViewModels()

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
            arguments?.getSerializable("currencyItemViewModels")?.let {
                currencyItemViewModels = it as CurrencyItemViewModels
            }

            currencyItemViewModelList = mapToViewModels(parseJson())
            fromCurrencySelectionSpinner =  binding.currencySelectionSpinnerFirstChoice
            toCurrencySelectionSpinner =  binding.currencySelectionSpinnerSecondChoice

            setUpCurrencySelectionSpinnerAdapter()
            setUpCurrencySelectionSpinnerListener()
            setUpCurrencySelectionViewModelObservers()
        }
    }

    private fun setUpCurrencySelectionSpinnerListener() {
        fromCurrencySelectionSpinner.onItemSelectedListener =
            getCurrencySelectionSpinnerListener(currencyItemViewModels.from)

        toCurrencySelectionSpinner.onItemSelectedListener =
            getCurrencySelectionSpinnerListener(currencyItemViewModels.to)
    }

    private fun setUpCurrencySelectionSpinnerAdapter() {
        binding.currencySelectionSpinnerFirstChoice.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it, currencyItemViewModelList)
            }

        binding.currencySelectionSpinnerSecondChoice.adapter =
            activity?.let {
                CurrencySelectionSpinnerAdapter(it,  currencyItemViewModelList)
            }

        if (currencyItemViewModelList.size > 1) {
            fromCurrencySelectionSpinner.setSelection(0)
            toCurrencySelectionSpinner.setSelection(1)
        }
    }

    private fun getCurrencySelectionSpinnerListener(viewModel: CurrencyItemViewModel) : AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCurrency = parent.getItemAtPosition(position) as CurrencyItemViewModel

                viewModel.updateData(
                    selectedCurrency.symbol.value,
                    selectedCurrency.country.value,
                    selectedCurrency.rate.value,
                    selectedCurrency.flagUrl.value,
                    selectedCurrency.imageName.value
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setUpCurrencySelectionViewModelObservers() {
        currencySelectionAmountViewModel.amount.observe(viewLifecycleOwner) {
            binding.currencySelectionAmount.text = getString(
                R.string.currency_selection_item_amount,
                currencySelectionAmountViewModel.amount.value
            )
        }

        currencySelectionConvertedAmountViewModel.convertedAmount.observe(viewLifecycleOwner) {
            binding.currencySelectionConvertedAmount.text = getString(
                R.string.currency_selection_converted_amount,
                currencySelectionConvertedAmountViewModel.convertedAmount.value
            )
        }

        currencySelectionConvertedAmountViewModel.exchangeRate.observe(viewLifecycleOwner) {
            Log.d("HENRIQUE", "exchangeRate -> ${currencySelectionConvertedAmountViewModel.exchangeRate.value}")

            val exchangeRateValue = currencySelectionConvertedAmountViewModel.exchangeRate.value
            if (!exchangeRateValue.isNullOrEmpty()) {
                binding.currencySelectionExchangeRate.visibility = View.VISIBLE
                binding.currencySelectionExchangeRate.text = currencySelectionConvertedAmountViewModel.exchangeRate.value
            } else {
                binding.currencySelectionExchangeRate.visibility = View.GONE
            }
        }
    }

    private fun parseJson(): List<CurrencyItem> {
        val listType = object : TypeToken<List<CurrencyItem>>() {}.type
        val bufferReader = resources.assets.open(FILENAME).bufferedReader()
        val jsonString = bufferReader.use { it.readText() }

        return Gson().fromJson(jsonString, listType)
    }

    private fun mapToViewModels(currencyItems: List<CurrencyItem>): List<CurrencyItemViewModel> {
        return currencyItems.map { item ->
            val viewModel = CurrencyItemViewModel()
            viewModel.updateData(item.symbol, item.country, item.rate, item.flagUrl, item.imageName)
            viewModel
        }
    }

    companion object {
        private const val FILENAME = "currencies_dummies.json"

        fun newInstance(currencyItemViewModels: CurrencyItemViewModels) =
            CurrencySelectionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("currencyItemViewModels", currencyItemViewModels)
                }
            }
    }
}