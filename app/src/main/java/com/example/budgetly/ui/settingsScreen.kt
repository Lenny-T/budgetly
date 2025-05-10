@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetly.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetly.R
import com.example.budgetly.data.currencies


val soraFont = FontFamily(
    Font(R.font.sora)
)

const val padding = 15

// MAIN SETTINGS
@Composable
fun SettingsScreen(currencyViewModel: currencyViewModel) {
    val currentCurrency = currencyViewModel.selectedCurrency.value
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        item {
            Text(
                text = stringResource(R.string.settingsHeading),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.Black
                ),
                modifier = Modifier.padding(padding.dp)
            )
            CurrencyChange(currency = currentCurrency ?: "USD", currencyViewModel = currencyViewModel)
        }
    }
}

// CHANGE APP CURRENCY
@Composable
fun CurrencyChange(currency: String, currencyViewModel: currencyViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    val viewModel: budgetlyViewModel = viewModel()
    var showPopUp = rememberSaveable { mutableStateOf(false) }
    val currencyCode by currencyViewModel.currencyCode.collectAsState()
    val currencyRate by currencyViewModel.currencyRate.collectAsState()

    // AVAILABLE CURRENCIES
    val dropdownList = listOf(
        currencies(
            currency = "USD",
            exchangeRate = 1.0,
            currencyLogo = painterResource(id = R.drawable.baseline_attach_money_24)
        ),
        currencies(
            currency = "GBP",
            exchangeRate = 0.78,
            currencyLogo = painterResource(id = R.drawable.baseline_currency_pound_24)
        ),
        currencies(
            currency = "EUR",
            exchangeRate = 0.92,
            currencyLogo = painterResource(id = R.drawable.baseline_euro_24)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = padding.dp, start = padding.dp),
    ) {
        // CURRENCY BOX
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color(0xFFffffff))
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .padding(end = 20.dp, start = 20.dp, top = 10.dp, bottom = 10.dp),
            ) {
                Text(
                    text = stringResource(R.string.currency),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = soraFont,
                        fontWeight = FontWeight.ExtraLight
                    )
                )
                // DROP DOWN
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val current = currencyViewModel.selectedCurrency.value
                    if (current != null) {
                        val currencySymbol = if (current == "GBP") " £" else if (current == "USD") " $" else " €"
                        Text(
                            text = current + currencySymbol,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = soraFont,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = "Drop Down",
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        dropdownList.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item.currency) },
                                leadingIcon = { Icon(item.currencyLogo, contentDescription = null) },
                                onClick = {
                                    currencyViewModel.setCurrency(item.currency)
                                    currencyViewModel.fetchCurrencyRate(item.currency)
                                    selectedCurrency = index
                                    Toast.makeText(
                                        context,
                                        "Currency Changed To ${item.currency}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    expanded = false
                                }
                            )
                        }
                    }
                }


            }

        }

        // DELETE ALL TRANSACTION IN DATABASE
        Button(
            modifier = Modifier.padding(top = 10.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
            ),
            onClick = {
                showPopUp.value = true
            }
        ) {
            Text(
                stringResource(R.string.delete_all_transactions),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.Light
                ),
                color = Color.White
            )
        }

        // INTENT BUTTON TO SHOW CURRENT RATES
        Button(
            modifier = Modifier.padding(top = 10.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF21C277),
            ),
            onClick = {
                // INTENT THAT MOVES TO GOOGLE TO GET CURRENCY RATES
                val intent = Intent(Intent.ACTION_VIEW).apply {
                     data = Uri.parse("https://www.google.com/search?q=${currencyViewModel.selectedCurrency.value}+rates")
                }
                context.startActivity(intent)
            }
        ) {
            Text(
                text = ("Current Rates"),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.Light
                ),
                color = Color.White
            )
        }

        // Text(currencyRate)

        // DELETE ALL TRANSACTION CONFIRMATION
        if (showPopUp.value){
            AlertDialog(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(20.dp),
                onDismissRequest = { showPopUp.value = false }
            ) {
                Column {
                    Text(
                        stringResource(R.string.are_you_sure_you_want_to_delete_all_your_transactions),
                        style = TextStyle(
                            fontSize = 23.sp,
                            fontFamily = soraFont,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.Black
                    )
                    Button(
                        modifier = Modifier.padding(top = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = {
                            viewModel.deleteAllTransactions() // RUN DELETE ALL TRANSACTION DAO
                            Toast.makeText(
                                context,
                                "Transactions Deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            showPopUp.value = false
                        }
                    ) {
                        Text(
                            stringResource(R.string.confirm),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = soraFont,
                                fontWeight = FontWeight.Light
                            ),
                            color = Color.White,
                        )
                    }
                }

            }
        }
    }
}
