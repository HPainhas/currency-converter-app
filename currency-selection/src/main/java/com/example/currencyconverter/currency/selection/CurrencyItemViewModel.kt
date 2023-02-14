package com.example.currencyconverter.currency.selection

data class CurrencyItemViewModel(
    val symbol: String,
    val country: String,
    val flag_url: String,
    val image_name: String,
    val rate: Float
)