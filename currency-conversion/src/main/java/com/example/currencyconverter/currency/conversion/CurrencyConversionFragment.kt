package com.example.currencyconverter.currency.conversion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.conversion.databinding.CurrencyConversionFragmentBinding
import com.example.currencyconverter.currency.selection.*
import com.example.currencyconverter.util.Util

class CurrencyConversionFragment : Fragment(R.layout.currency_conversion_fragment) {

    private lateinit var handler: Handler
    private lateinit var binding: CurrencyConversionFragmentBinding
    private lateinit var currencyItemViewModels: CurrencyItemViewModels

    private val currencySelectionAmountViewModel: CurrencySelectionAmountViewModel by activityViewModels()
    private val currencySelectionConvertedAmountViewModel: CurrencySelectionConvertedAmountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyConversionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        if (savedInstanceState == null) {
            arguments?.getSerializable("currencyItemViewModels")?.let {
                currencyItemViewModels = it as CurrencyItemViewModels
            }

            parentFragmentManager.commit {
                add(
                    R.id.currency_conversion_fragment_container_view,
                    CurrencySelectionFragment.newInstance(currencyItemViewModels)
                )
            }

            setUpCurrencyAmountEditTextListener()

            binding.currencyConversionConvertButton.setOnClickListener {
                val enteredAmount = binding.currencyConversionAmount.text

                if (!enteredAmount.isNullOrEmpty()) {
                    updateCurrencySelectionViewModels(enteredAmount.toString())
                }
            }
        }
    }

    private fun updateCurrencySelectionViewModels(enteredAmount: String) {
        val enteredAmountWithoutCommas = Util.removeAllNonNumericCharacters(enteredAmount).toDouble()
        val convertedAmount = enteredAmountWithoutCommas / currencyItemViewModels.to.rate.value!!

        Log.d("HENRIQUE", "from.symbol -> ${currencyItemViewModels.from.symbol.value!!}")
        Log.d("HENRIQUE", "to.rate -> ${currencyItemViewModels.from.rate.value!!}")
        Log.d("HENRIQUE", "to.symbol -> ${currencyItemViewModels.to.symbol.value!!}")

        currencySelectionAmountViewModel.updateAmount(enteredAmount)
        currencySelectionConvertedAmountViewModel.updateConvertedAmount(convertedAmount)
        currencySelectionConvertedAmountViewModel.updateExchangeRate(
            requireContext(),
            currencyItemViewModels.from.symbol.value!!,
            currencyItemViewModels.to.rate.value!!,
            currencyItemViewModels.to.symbol.value!!
        )
    }

    private fun setUpCurrencyAmountEditTextListener() {
        val editTextAmount = binding.currencyConversionAmount

        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) return

                val cleanString = Util.removeAllNonNumericCharacters(s.toString())
                val formattedString = Util.getCommaFormattedString(cleanString)

                if (s.toString() != formattedString) {
                    editTextAmount.setText(formattedString)
                    editTextAmount.setSelection(formattedString.length)
                }

                // TODO - Observer the changes after every character by calling
                //  currencyExchangeViewModel.amount.observe(viewLifecycleOwner)
            }

            override fun afterTextChanged(s: Editable?) {
                val cleanString = Util.removeAllNonNumericCharacters(s.toString())

                if (cleanString.length >= MAX_LENGTH) {
                    editTextAmount.error = "Maximum amount reached"
                    handler.postDelayed({
                        editTextAmount.error = null
                    }, ERROR_TIMEOUT)
                } else {
                    editTextAmount.error = null
                    handler.removeCallbacksAndMessages(null)
                }
            }
        })
    }

    companion object {
        private const val MAX_LENGTH = 10
        private const val ERROR_TIMEOUT = 5000L

        fun newInstance(currencyItemViewModels: CurrencyItemViewModels) =
            CurrencyConversionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("currencyItemViewModels", currencyItemViewModels)
                }
            }
    }
}