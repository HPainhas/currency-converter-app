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
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.conversion.databinding.CurrencyConversionFragmentBinding
import com.example.currencyconverter.currency.selection.CurrencySelectionFragment
import java.text.DecimalFormat

class CurrencyConversionFragment : Fragment(R.layout.currency_conversion_fragment) {

    private val MAX_LENGTH = 10
    private val ERROR_TIMEOUT = 5000L

    private lateinit var handler: Handler
    private lateinit var binding: CurrencyConversionFragmentBinding

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

        parentFragmentManager.commit {
            add(
                R.id.currency_conversion_fragment_container_view,
                CurrencySelectionFragment()
            )
        }

        handler = Handler(Looper.getMainLooper())

        setUpEditTextListener()
    }

    private fun setUpEditTextListener() {
        val editTextAmount = binding.currencyConversionAmount

        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) return

                val cleanString = getCleanString(s.toString())
                val formattedString = getFormattedString(cleanString)

                if (s.toString() != formattedString) {
                    editTextAmount.setText(formattedString)
                    editTextAmount.setSelection(formattedString.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val cleanString = getCleanString(s.toString())

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

    private fun getCleanString(originalString: String) =
        originalString.replace("\\D".toRegex(), "") // remove all non-numeric characters

    private fun getFormattedString(s: String) =
        DecimalFormat("#,###,###,###").format(s.toLong())

}