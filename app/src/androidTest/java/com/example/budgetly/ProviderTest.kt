package com.example.budgetly

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.budgetly.provider.budgetlyContract
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProviderTest {
    private lateinit var context: Context
    private lateinit var resolver: ContentResolver
    private val testUri: Uri = budgetlyContract.Transactions.CONTENT_URI
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        resolver = context.contentResolver
    }
    @Test
    fun testGetAllTransactions() {
        val values = ContentValues().apply {
            put(budgetlyContract.Transactions.COLUMN_DESCRIPTION, "Sample Transaction")
            put(budgetlyContract.Transactions.COLUMN_DATE, "08/05/2025")
            put(budgetlyContract.Transactions.COLUMN_AMOUNT, 100.0)
            put(budgetlyContract.Transactions.COLUMN_TYPE, "EXPENSE")
        }
        // INSERTS TO DATABASE USING THE CONTENT PROVIDER
        resolver.insert(testUri, values)

        // QUERY THE DATA
        val cursor: Cursor? = resolver.query(testUri, null, null, null, null)
        assertNotNull("Cursor is null", cursor)
        assertTrue("Cursor is empty", cursor?.moveToFirst() == true)

        val amount = cursor?.getDouble(cursor.getColumnIndexOrThrow(budgetlyContract.Transactions.COLUMN_AMOUNT))
        assertEquals(100.0, amount)
        cursor?.close()
    }
    @Test
    fun testInsert() {
        val values = ContentValues().apply {
            put(budgetlyContract.Transactions.COLUMN_DESCRIPTION, "Test Transaction")
            put(budgetlyContract.Transactions.COLUMN_DATE, "08/05/2025")
            put(budgetlyContract.Transactions.COLUMN_AMOUNT, 50.0)
            put(budgetlyContract.Transactions.COLUMN_TYPE, "INCOME")
        }

        val uri = resolver.insert(testUri, values)
        assertNotNull("Insertion failed", uri)

        // VERIFY INSERTED DATA
        val cursor = resolver.query(uri!!, null, null, null, null)
        assertTrue("Cursor is empty", cursor?.moveToFirst() == true)

        val description = cursor?.getString(cursor.getColumnIndexOrThrow(budgetlyContract.Transactions.COLUMN_DESCRIPTION))
        assertEquals("Test Transaction", description)
        cursor?.close()
    }
    @Test
   fun testDeleteTransactions() {
        val values = ContentValues().apply {
            put(budgetlyContract.Transactions.COLUMN_DESCRIPTION, "To be deleted")
            put(budgetlyContract.Transactions.COLUMN_AMOUNT, 15.0)
        }
        val uri = resolver.insert(testUri, values)

        // DELETE THE DATA
        val rowsDeleted = resolver.delete(uri!!, null, null)
        assertEquals(1, rowsDeleted)

        // ENSURE IT WAS DELETED
        val cursor = resolver.query(uri, null, null, null, null)
        assertTrue("Record not deleted", cursor?.count == 0)
        cursor?.close()
   }
    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        resolver.query(Uri.parse("content://com.example.budgetly.provider/invalid"), null, null, null, null)
    }
    @After
    fun tearDown() { /* Clear the database if needed after test */ }
}