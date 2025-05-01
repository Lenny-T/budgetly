package com.example.budgetly.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class transactionsRepository(private val TransactionDao:transactionDao) {

    // RETRIEVE ALL TRANSACTIONS
    val get_all_transactions: LiveData<List<Transactions>> = TransactionDao.getTransactions()

    // INSERT A TRANSACTION
    suspend fun insert_transaction(transactions: Transactions){
        TransactionDao.insert(transactions)
    }

    // DELETE A TRANSACTION
    suspend fun delete_transaction(transactions: Transactions){
        TransactionDao.deleteTransaction(transactions.transactionID)
    }

    // DELETE EVERYTHING
    suspend fun deleteAllTransactions() {
        TransactionDao.deleteAllTransactions()
    }

    // GET ALL TOTAL INCOME
    suspend fun getTotalType(type: transactionType):Double {
        return if (TransactionDao.getTotalType(type) == null){
            0.0
        } else{
            TransactionDao.getTotalType(type)
        }

    }

}