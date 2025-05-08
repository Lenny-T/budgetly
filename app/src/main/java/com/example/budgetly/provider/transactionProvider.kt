package com.example.budgetly.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.media.tv.TvContract
import android.net.Uri
import com.example.budgetly.data.Transactions
import com.example.budgetly.data.transactionDao
import com.example.budgetly.data.transactionDatabase
import com.example.budgetly.data.transactionType

class transactionProvider : ContentProvider() {
    private lateinit var TransactionDao: transactionDao

    /*
        OVERRIDE ON CREATE
        -> INITIALISES THE CONTENT PROVIDER
        -> CALLS THIS FUNCTION IMMEDIATELY AFTER THE PROVIDER IS CREATED
    */
    override fun onCreate(): Boolean {
        // GET AN INSTANCE OF THE transactionDao TO USE THE ROOM DATABASE
        TransactionDao = transactionDatabase.getDatabase(context!!).TransactionDao()
        return true // SUCCESSFULLY LOADED
    }

    companion object{
        private val transactions = 100
        private val transactionsID = 101
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(budgetlyContract.AUTHORITY, budgetlyContract.Transactions.PATH_TRANSACTION, transactions)
            addURI(budgetlyContract.AUTHORITY, "${budgetlyContract.Transactions.PATH_TRANSACTION}/#", transactionsID)
        }
    }

    /*
        OVERRIDE QUERY
        -> RETRIEVES DATA FROM THE PROVIDER.
        -> USES THE ARGUMENTS TO SELECT THE TABLE TO QUERY.
        -> USES THE ARGUMENTS TO SELECT THE ROWS AND COLUMNS TO RETURN AND THE SORT ORDER.
        -> RETURNS THE DATA AS A CURSOR.

        override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {}
    */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val match = uriMatcher.match(uri)
        return when (match) {
            transactions -> TransactionDao.getAllTransactions()
            transactionsID -> {
                val id = ContentUris.parseId(uri)
                TransactionDao.getAllTransactionsWithID(id.toInt())
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }


    /*
        OVERRIDE INSERT
        -> INSERT A NEW ROW TO THE PROVIDER.
        -> USE ARGUMENTS TO SELECT DESTINATION TABLE AND TO GET THE COLUMN VALUES TO USE.
        -> RETURNS A CONTENT URI FOR THE INSERTED ROW.

        override fun insert(): Uri? {}
    */

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val newRowId = when (uriMatcher.match(uri)) {
            transactions -> {
                val transaction = Transactions(
                    description = values?.getAsString(budgetlyContract.Transactions.COLUMN_DESCRIPTION) ?: "",
                    date = values?.getAsString(budgetlyContract.Transactions.COLUMN_DATE) ?: "",
                    amount = values?.getAsDouble(budgetlyContract.Transactions.COLUMN_AMOUNT) ?: 0.0,
                    transactionType = values?.getAsString(budgetlyContract.Transactions.COLUMN_TYPE)?.let {
                        enumValueOf<transactionType>(it)
                    } ?: transactionType.INCOME
                )
                TransactionDao.insertTransaction(transaction)
            }
            else -> throw IllegalArgumentException("Invalid URI for insert: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, newRowId)
    }


    /*
        OVERRIDE UPDATE
        -> UPDATE EXISTING ROWS IN THE PROVIDER
        -> USE THE ARGUMENTS TO SELECT THE TABLE AND ROWS TO UPDATE AND GET THE UPDATED COLUMN
        -> RETURNS THE NUMBER OF ROWS UPDATED.

        override fun update(): Int {}
    */
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Update Operation Is Not Supported")
    }


    /*
         OVERRIDE GET TYPE
         -> RETURNS THE MIME TYPE CORRESPONDING TO A CONTENT URI.

        override fun getType(): String? {}
    */
    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            transactions -> budgetlyContract.Transactions.CONTENT_TYPE
            transactionsID -> budgetlyContract.Transactions.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }


    /*
        OVERRIDE DELETE
        -> DELETES A ROW FROM THE CONTENT PROVIDER.
        -> USES THE ARGUMENTS TO SELECT THE TABLE AND ROWS TO DELETE.
        -> RETURNS THE NUMBER OF ROWS TO DELETE

        override fun delete(): Int {}
    */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val context = context ?: return 0
        val match = uriMatcher.match(uri)
        return when (match) {
            transactionsID -> {
                val id = ContentUris.parseId(uri).toInt()
                val cursor = TransactionDao.getAllTransactionsWithID(id)
                if (cursor.moveToFirst()) {
                    val transaction = Transactions(
                        transactionID = cursor.getInt(cursor.getColumnIndexOrThrow("transactionID")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                        transactionType = cursor.getString(cursor.getColumnIndexOrThrow("transactionType"))?.let {
                            enumValueOf<transactionType>(it)
                        } ?: transactionType.INCOME
                    )
                    cursor.close()
                    TransactionDao.deleteMovie(transaction)
                } else {
                    cursor.close()
                    0
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}