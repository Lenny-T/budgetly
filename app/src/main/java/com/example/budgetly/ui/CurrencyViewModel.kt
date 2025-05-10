package com.example.budgetly.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class currencyViewModel : ViewModel() {
    private val _selectedCurrency = MutableLiveData("EUR")
    val selectedCurrency: LiveData<String> = _selectedCurrency

    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

    // API TO GET THE CURRENT RATE WITH BASE AS DOLLARS
    private val currencyRatesMap = mutableMapOf<String, Double>()
    private val _currencyRate = MutableStateFlow<String>("1.0")
    val currencyRate: StateFlow<String> = _currencyRate
    private val _currencyCode = MutableStateFlow<String>("")
    val currencyCode: StateFlow<String> = _currencyCode

    private val apiKey = "b3c51616211b118b8e36d86d93bf6a92"

    // Fetches the currency rate from API and stores it
    fun fetchCurrencyRate(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Check if the rate is already cached
                if (currencyRatesMap.containsKey(currency)) {
                    val rate = currencyRatesMap[currency].toString()
                    _currencyCode.value = currency
                    _currencyRate.value = rate
                    Log.d("fetchCurrencyRate", "Cached Rate for $currency: $rate")
                } else {
                    // Fetch the rate if not cached
                    val (code, rate) = getRate(currency)
                    if (rate.isNotEmpty()) {
                        _currencyCode.value = code
                        _currencyRate.value = rate
                        // Save the rate to the map for future use
                        currencyRatesMap[code] = rate.toDouble()
                    } else {
                        _currencyRate.value = "Error: Failed to fetch rate."
                    }
                }
            } catch (e: Exception) {
                _currencyRate.value = "Error: Unable to retrieve rate."
                e.printStackTrace()
            }
        }
    }

    // Sends a GET request to the URL and returns a JSON string
    private fun getJSONFromApi(url: String): String {
        var result = ""
        var conn: HttpsURLConnection? = null
        try {
            val request = URL(url)
            conn = request.openConnection() as HttpsURLConnection
            conn.connect()
            val inStream: InputStream = conn.inputStream
            result = BufferedReader(InputStreamReader(inStream)).use { it.readText() }
        } catch (e: Exception) {
            result = "{\"success\": false, \"error\": \"Network Error!\"}"
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
        return result
    }

    // Gets the current rate of the selected currency
    private fun getRate(currency: String): Pair<String, String> {
        val url1 = "https://api.exchangeratesapi.io/v1/latest?access_key=$apiKey&symbols=$currency&format=1"
        var rate = ""
        try {
            val response = getJSONFromApi(url1)
            val jsonObject = JSONObject(response)

            if (jsonObject.getBoolean("success")) {
                val rates = jsonObject.getJSONObject("rates")
                rate = rates.getDouble(currency).toString()
            }
        } catch (e: Exception) {
            rate = "Error: Parsing failed"
            e.printStackTrace()
        }
        return Pair(currency, rate)
    }
}