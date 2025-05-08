package com.example.budgetly.provider

import android.net.Uri

object budgetlyContract {
    const val AUTHORITY = "com.example.budgetly.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
    object Transactions {
        const val PATH_TRANSACTION = "Transactions"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRANSACTION)
        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_TRANSACTION"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_TRANSACTION"

        const val COLUMN_ID = "transactionID"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_TYPE = "transactionType"
    }
}