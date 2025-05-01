package com.example.budgetly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transactions::class], version =1, exportSchema = false)
abstract class transactionDatabase:RoomDatabase(){
    abstract fun TransactionDao(): transactionDao
    companion object {
        @Volatile
        private var Instance: transactionDatabase? = null
        fun getDatabase(context: Context):transactionDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, transactionDatabase::class.java, "transaction")
                    .build().also { Instance = it }
            }
        }
    }
}