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
    private lateinit var firstCurrencySelectionSpinner: Spinner
    private lateinit var secondCurrencySelectionSpinner: Spinner
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
            firstCurrencySelectionSpinner =  binding.currencySelectionSpinnerFirstChoice
            secondCurrencySelectionSpinner =  binding.currencySelectionSpinnerSecondChoice

            setUpCurrencySelectionSpinnerAdapter()
            setUpCurrencySelectionSpinnerListener()
            setUpCurrencyExchangeViewModelObserver()
        }
    }

    private fun setUpCurrencySelectionSpinnerListener() {
        firstCurrencySelectionSpinner.onItemSelectedListener =
            getCurrencySelectionSpinnerListener(currencyItemViewModels.first)

        secondCurrencySelectionSpinner.onItemSelectedListener =
            getCurrencySelectionSpinnerListener(currencyItemViewModels.second)
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
            firstCurrencySelectionSpinner.setSelection(0)
            secondCurrencySelectionSpinner.setSelection(1)
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

    private fun setUpCurrencyExchangeViewModelObserver() {
        currencySelectionAmountViewModel.amount.observe(viewLifecycleOwner) {
            Log.d("HENRIQUE", "amount -> ${currencySelectionAmountViewModel.amount.value}")
            Log.d("HENRIQUE", "convertedAmount -> ${currencySelectionConvertedAmountViewModel.convertedAmount.value}")
            Log.d("HENRIQUE", "exchangeRate -> ${currencySelectionConvertedAmountViewModel.exchangeRate.value}")

            binding.currencySelectionAmount.text =
                currencySelectionAmountViewModel.amount.value
            binding.currencySelectionConvertedAmount.text =
                currencySelectionConvertedAmountViewModel.convertedAmount.value
            binding.currencySelectionExchangeRate.text =
                currencySelectionConvertedAmountViewModel.exchangeRate.value
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