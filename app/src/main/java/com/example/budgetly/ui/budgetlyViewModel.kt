package com.example.budgetly.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.Transactions
import com.example.budgetly.data.transactionDatabase
import com.example.budgetly.data.transactionType
import com.example.budgetly.data.transactionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class budgetlyViewModel(application:Application):AndroidViewModel(application) {
    private val transactionsRepo: transactionsRepository
    init {
        val transactionDao = transactionDatabase.getDatabase(application).TransactionDao()
        transactionsRepo = transactionsRepository(transactionDao)
    }

    // GET ALL DATA IN THE DATABASE
    val transactionData = transactionsRepo.get_all_transactions

    // ADD TRANSACTION TO THE DATABASE
    fun insertTransaction(transaction: Transactions) {
        viewModelScope.launch {
            transactionsRepo.insert_transaction(transaction)
        }
    }

    // DELETE TRANSACTION TO THE DATABASE
    fun deleteTransaction(transaction: Transactions) {
        viewModelScope.launch {
            transactionsRepo.delete_transaction(transaction)
        }
    }

    // DELETE EVERYTHING
    fun deleteAllTransactions() = viewModelScope.launch {
        transactionsRepo.deleteAllTransactions()
    }

    // GET THE TOTAL INCOME OR EXPENSE
    fun getTotalType(type: transactionType, onResult: (Double) -> Unit) {
        viewModelScope.launch {
            val totalIncome = transactionsRepo.getTotalType(type)
            Log.d("getTotalType", "Total result: $totalIncome")
            onResult(totalIncome)
        }
    }

}