package com.example.currencyconverter.api

interface ApiResponseCallback {
    fun onSuccessApiResponse(responseBody: String, identifier: String)
    fun onFailureApiResponse(errorMessage: String)
}