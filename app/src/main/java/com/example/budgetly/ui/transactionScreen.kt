package com.example.budgetly.ui


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetly.R
import com.example.budgetly.data.Transactions
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsPage (currencyViewModel: currencyViewModel){
    val selectedCurrency = currencyViewModel.selectedCurrency.value
    val viewModel: budgetlyViewModel = viewModel()
    var showPopUp by remember { mutableStateOf(false) }
    var transactionType by rememberSaveable { mutableStateOf("Income") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(TextFieldValue(""))}
    val transactions by viewModel.transactionData.observeAsState(emptyList())
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
    ){
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextStyle(stringResource(R.string.add_transaction), 28, FontWeight.Black, Color.Black)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF21C277))
                        .clickable {
                            showPopUp = true
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add Transaction",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White,
                    )
                }
            }

            if (showPopUp){
                AlertDialog(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(20.dp),
                    onDismissRequest = { showPopUp = false }
                ) {
                    Column {
                        // INCOME AND EXPENSE TOGGLES
                        Row {
                            // Income Button
                            TransactionTypeButton(
                                stringResource(R.string.income),
                                transactionType == "Income"
                            ) { transactionType = "Income" }
                            // Expense Button
                            TransactionTypeButton(
                                stringResource(R.string.expense),
                                transactionType == "Expense"
                            ) { transactionType = "Expense" }
                        }
                        // FORM
                        // Amount Text Field
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { input ->
                                if (input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    amount = input
                                }
                            },
                            label = { Text(stringResource(R.string.amount)) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        // Description Text Field
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            label = { Text(stringResource(R.string.description)) },
                        )
                        // Date Text Field
                        OutlinedTextField(
                            value = date,
                            onValueChange = { input ->
                                val digits = input.text.filter { it.isDigit() }
                                val formatted = buildString {
                                    for (i in digits.indices) {
                                        append(digits[i])
                                        if ((i == 1 || i == 3) && i != digits.lastIndex) append('/')
                                    }
                                }
                                val newCursorPosition = when {
                                    formatted.length > input.text.length && (formatted.length == 3 || formatted.length == 6) -> input.selection.start + 3
                                    else -> input.selection.start
                                }
                                if (formatted.length <= 10) {
                                    date = TextFieldValue(
                                        text = formatted,
                                        selection = TextRange(newCursorPosition.coerceAtMost(formatted.length))
                                    )
                                }
                            },
                            label = { Text(stringResource(R.string.date_dd_mm_yyyy)) },
                            placeholder = { Text(stringResource(R.string.dd_mm_yyyy)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // ADD TRANSACTION BUTTON
                        Button(
                            modifier = Modifier.padding(top = 5.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF21C277),
                                contentColor = Color.White
                            ),
                            onClick = {
                                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                                val type = if (transactionType == "Income") com.example.budgetly.data.transactionType.INCOME else com.example.budgetly.data.transactionType.EXPENSE
                                val newTransaction = Transactions(
                                    transactionID = 0, // Let Room auto-generate
                                    description = description,
                                    date = date.text,
                                    amount = amountDouble,
                                    transactionType = type
                                )
                                viewModel.insertTransaction(newTransaction)
                                showPopUp = false
                                Toast.makeText(
                                    context,
                                    "Transaction Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                amount = ""
                                description = ""
                                date = TextFieldValue("")
                            }
                        ) {
                            TextStyle(stringResource(R.string.add_transaction_2), 15, FontWeight.Black, Color.White)
                        }

                    }
                }
            }
        }
        if (transactions.isEmpty()) {
            item {
                TextStyle(stringResource(R.string.no_transactions), 20, FontWeight.Light, Color(0xFF21C277))
            }
        } else {
            items(transactions) { transaction ->
                TransactionItem(transaction, selectedCurrency ?: "USD", viewModel, "Transaction")
            }
        }
    }
}


// TRANSACTION TYPE BUTTON
@Composable
fun TransactionTypeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) Color(0xFF21C277) else Color.White
    val contentColor = if (isSelected) Color.White else Color(0xFF21C277)
    val borderColor = if (isSelected) Color.Transparent else Color(0xFF21C277)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = soraFont,
                fontWeight = FontWeight.Black
            ),
        )
    }
}

// TEXT STYLE COMPOSABLE
@Composable
fun TextStyle (text : String, size : Int, weight: FontWeight, colour : Color){
    Text(
        text,
        style = TextStyle(
            fontSize = size.sp,
            fontFamily = soraFont,
            fontWeight = weight
        ),
        color = colour
    )
}

@Composable
fun TransactionItem (transaction: Transactions, selectedCurrency : String, viewModel: budgetlyViewModel, page: String){
    val boxWidth = 300f
    val maxSwipeLeft = -boxWidth * 0.7f
    var offsetX by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(Color.Red)
    ) {
        if (page == "Transaction"){
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Transaction",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 25.dp)
                    .size(30.dp)
                    .clickable { viewModel.deleteTransaction(transaction) },
                tint = Color.White,
            )
        }


        Box(
            modifier = Modifier
                .then(
                    if (page == "Transaction") {
                        Modifier
                            .offset { IntOffset(offsetX.roundToInt(), 0) }
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    val newOffset = offsetX + delta
                                    offsetX = newOffset.coerceIn(maxSwipeLeft, 0f)
                                }
                            )
                    } else {
                        Modifier
                    }
                )
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(
                    if (transaction.transactionType == com.example.budgetly.data.transactionType.INCOME)
                        Color(0xFF21C277)
                    else
                        Color.White
                )
                .then(
                    if (transaction.transactionType == com.example.budgetly.data.transactionType.EXPENSE)
                        Modifier.border(
                            width = 2.dp,
                            color = Color(0xFF21C277),
                            RoundedCornerShape(8.dp)
                        )
                    else Modifier
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Column {
                    val textColor = if (transaction.transactionType == com.example.budgetly.data.transactionType.INCOME)
                        Color.White else Color(0xFF21C277)

                    TextStyle(transaction.description, 22, FontWeight.Black, textColor)
                    TextStyle(transaction.date, 18, FontWeight.Light, textColor)
                }

                val amountPrefix = if (transaction.transactionType == com.example.budgetly.data.transactionType.INCOME) "+ " else "- "
                val amountColor = if (transaction.transactionType == com.example.budgetly.data.transactionType.INCOME)
                    Color.White else Color(0xFF21C277)
                val currencySymbol = if (selectedCurrency == "GBP") "£ " else if (selectedCurrency == "USD") "$ " else "€ "
                val convertedAmount = transaction.amount
                TextStyle(amountPrefix + currencySymbol + convertedAmount.toString(), 22, FontWeight.Black, amountColor)
            }
        }
    }

}

