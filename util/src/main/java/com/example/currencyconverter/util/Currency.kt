package com.example.currencyconverter.util

data class Currency(
    val symbol: String,
    val countryName: String,
    val countryCode: String,
    var rate: Double,
    val imageName: String
)