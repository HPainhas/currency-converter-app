package com.example.currencyconverter.currency.conversion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.conversion.databinding.CurrencyConversionFragmentBinding
import com.example.currencyconverter.currency.selection.*
import com.example.currencyconverter.util.Util
import java.text.DecimalFormat

class CurrencyConversionFragment : Fragment(R.layout.currency_conversion_fragment) {

    private lateinit var handler: Handler
    private lateinit var binding: CurrencyConversionFragmentBinding

    private val currencySelectionAmountViewModel: CurrencySelectionAmountViewModel by activityViewModels()

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

        if (savedInstanceState == null) {
            handler = Handler(Looper.getMainLooper())

            loadCurrencySelectionFragment()
            setUpCurrencyAmountEditTextListener()

            binding.currencyConversionConvertButton.setOnClickListener {
                val enteredAmount = binding.currencyConversionAmountEditText.text

                if (!enteredAmount.isNullOrEmpty()) {
                    val amount =
                        Util.removeDollarSignAndCommas(enteredAmount.toString()).toDouble()

                    currencySelectionAmountViewModel.updateAmount(amount)
                }
            }
        }
    }

    private fun loadCurrencySelectionFragment() {
        parentFragmentManager.commit {
            add(
                R.id.currency_conversion_fragment_container_view,
                CurrencySelectionFragment()
            )
        }
    }

    private fun setUpCurrencyAmountEditTextListener() {
        var current = ""
        val editTextAmount = binding.currencyConversionAmountEditText

        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            // TODO - Observer that changes after every character by calling

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) return

                val originalString = s.toString()
                val cleanString = Util.removeAllNonNumericCharacters(originalString)

                if (cleanString.length > MAX_LENGTH_CLEAN_STRING) {
                    editTextAmount.setText(current)
                    editTextAmount.setSelection(current.length)
                    return
                }

                if (originalString != current) {
                    editTextAmount.removeTextChangedListener(this)

                    val cleanParsedString =
                        Util.removeDollarSignCommasAndDecimalPoints(originalString).toDouble()
                    val formatter = DecimalFormat(CURRENCY_PATTERN)
                    val formatted = formatter.format((cleanParsedString / 100))

                    current = formatted
                    editTextAmount.setText(formatted)
                    editTextAmount.setSelection(formatted.length)

                    editTextAmount.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val originalString = s.toString()
                val cleanString = Util.removeAllNonNumericCharacters(originalString)

                if (cleanString.length >= MAX_LENGTH_CLEAN_STRING) {
                    editTextAmount.error = getString(R.string.currency_conversion_amount_edit_text_error)
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
        private const val ERROR_TIMEOUT = 5000L
        private const val CURRENCY_PATTERN = "$ #,###.00"
        private const val MAX_LENGTH_CLEAN_STRING = 12
    }
}