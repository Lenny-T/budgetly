package com.example.budgetly.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.Currency
import javax.net.ssl.HttpsURLConnection


class currencyViewModel : ViewModel() {
    private val _selectedCurrency = MutableLiveData("USD")
    val selectedCurrency: LiveData<String> = _selectedCurrency

    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

    // API TO GET THE CURRENT RATE WITH BASE AS DOLLARS
    private val apiKey = "b3c51616211b118b8e36d86d93bf6a92"
    fun fetchCurrencyData(currency: String) {
        viewModelScope.launch (Dispatchers.IO) {

        }
    }
}