package com.example.currencyconverter.currency.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.favorite.databinding.CurrencyFavoriteFragmentBinding

class CurrencyFavoriteFragment : Fragment(R.layout.currency_favorite_fragment) {

    private lateinit var binding: CurrencyFavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyFavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HENRIQUE", "CurrencyFavoriteFragment displayed")
    }
}