package com.example.budgetly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true) val transactionID:Int = 0,
    val description:String,
    val date:String,
    val amount:Double,
    val transactionType:transactionType
)