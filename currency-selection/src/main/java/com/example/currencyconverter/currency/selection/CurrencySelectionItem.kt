package com.example.currencyconverter.currency.selection

data class CurrencySelectionItem(
    val symbol: String,
    val country: String,
    val rate: Double,
    val flagUrl: String,
    val imageName: String
)
