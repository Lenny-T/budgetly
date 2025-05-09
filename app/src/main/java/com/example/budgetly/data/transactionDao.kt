package com.example.budgetly.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface transactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // THIS INSERTS AN INSTANCE OF THE CLASS TRANSACTIONS
    suspend fun insert(transaction: Transactions)

    @Query("SELECT * FROM Transactions") // GET ALL THE TRANSACTIONS
    fun getTransactions(): LiveData<List<Transactions>>

    @Query("DELETE FROM Transactions WHERE :transactionId = transactionID") // DELETE A TRANSACTION
    suspend fun deleteTransaction(transactionId: Int)

    @Query("DELETE FROM Transactions") // DELETE ALL
    suspend fun deleteAllTransactions()

    @Query("SELECT SUM(amount) FROM Transactions WHERE transactionType = :type")
    suspend fun getTotalType(type: transactionType): Double

    // FOR THE CONTENT PROVIDER
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTransaction(transaction: Transactions):Long
    @Query("SELECT * FROM Transactions")
    fun getAllTransactions(): Cursor
    @Query("SELECT * FROM Transactions WHERE transactionID = :id")
    fun getAllTransactionsWithID(id: Int): Cursor
    @Delete
    fun deleteMovie(transaction: Transactions): Int

}