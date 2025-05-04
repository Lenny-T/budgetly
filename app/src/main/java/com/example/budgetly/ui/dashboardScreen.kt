package com.example.budgetly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.budgetly.R
import com.example.budgetly.data.appScreens
import com.example.budgetly.data.transactionType


@Composable
fun DashboardScreen (
    currencyViewModel: currencyViewModel,
    navController: NavHostController // FOR VIEW ALL NAVIGATION
){
    val selectedCurrency = currencyViewModel.selectedCurrency.value
    val currencySymbol = if (selectedCurrency == "GBP" ) "£ " else if (selectedCurrency == "USD") "$ " else "€ "
    val viewModel: budgetlyViewModel = viewModel()
    val transactions by viewModel.transactionData.observeAsState(emptyList())
    var totalIncome by remember { mutableDoubleStateOf(0.0) }
    var totalIncomeMessage by remember { mutableStateOf("0.0") }
    // RUNS THE THE GET TOTAL ENDPOINT ON THE INCOME
    LaunchedEffect(Unit) {
        viewModel.getTotalType(transactionType.INCOME) { total ->
            totalIncome = total
            totalIncomeMessage = totalIncome.toString()
        }
    }
    var totalExpense by remember { mutableDoubleStateOf(0.0) }
    var totalExpenseMessage by remember { mutableStateOf("0.0") }
    // RUNS THE GET TOTAL ENDPOINT ON THE EXPENSES
    LaunchedEffect(Unit) {
        viewModel.getTotalType(transactionType.EXPENSE) { total ->
            totalExpense = total
            totalExpenseMessage = totalExpense.toString()
        }
    }
    var budget by remember { mutableDoubleStateOf(0.0) }
    budget = totalIncome - totalExpense
    val spacing = 13.dp

    LazyColumn(
        modifier = Modifier
            .padding(end = padding.dp, start = padding.dp)
            .fillMaxHeight()
    ){
        // HEADING
        item {
            Spacer(modifier = Modifier.height(spacing))
            Text(
                text = stringResource(R.string.welcome),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.Black
                ),
            )
            Spacer(modifier = Modifier.height(spacing))
        }

        // MY BUDGET WIDGET
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF21C277))
                    .padding(top = 20.dp, bottom = 20.dp),
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.my_budget),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = soraFont,
                            fontWeight = FontWeight.Light
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = stringResource(R.string.budget_amount, currencySymbol, budget),
                        style = TextStyle(
                            fontSize = 35.sp,
                            fontFamily = soraFont,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color.White
                    )
                }

            }
            Spacer(modifier = Modifier.height(spacing))
        }

        // INCOME AND EXPENSES WIDGETS
        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding.dp, bottom = padding.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(18.dp),
                            clip = true
                        )
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    IncomeExpensesWidget(stringResource(R.string.income2), currencySymbol, totalIncomeMessage)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(18.dp),
                            clip = true
                        )
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    IncomeExpensesWidget(stringResource(R.string.Expense_Widget_Title), currencySymbol, totalExpenseMessage)
                }
            }
            Spacer(modifier = Modifier.height(spacing))
        }

        // TRANSACTION WIDGET
        item{
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(18.dp),
                        clip = true
                    )
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(padding.dp)
            ) {
                Column{
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = stringResource(R.string.transactions_2),
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontFamily = soraFont,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.Black,
                        )

                        Text(
                            text = stringResource(R.string.view_all),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = soraFont,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                navController.navigate(route = appScreens.Transactions.name)
                            }
                        )
                    }

                    if (transactions.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_transactions),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = soraFont,
                                fontWeight = FontWeight.Light
                            ),
                            color = Color(0xFF21C277)
                        )
                    } else {
                        // SHOW A MAXIMUM OF 3 TRANSACTIONS
                        transactions.take(3).forEach { transaction ->
                            TransactionItem(transaction, selectedCurrency ?: "USD", viewModel, "Home")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


// INCOME AND EXPENSES BOX
@Composable
fun IncomeExpensesWidget(type: String, currency: String, value: String){
    val spacing = 8
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF21C277))
                    .padding(13.dp)
            ){
                if (type == "Income"){
                    Image(
                        painter = painterResource(id = R.drawable.income),
                        contentDescription = stringResource(R.string.Expense_Icon),
                        modifier = Modifier.size(40.dp)
                    )
                } else{
                    Image(
                        painter = painterResource(id = R.drawable.payments), // Replace with your vector's name
                        contentDescription = stringResource(R.string.income_icon),
                        modifier = Modifier.size(40.dp) // Adjust the size as needed
                    )
                }

            }
            Spacer(modifier = Modifier.height(spacing.dp))
            Text(
                text = type,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(spacing.dp))
            Text(
                text = stringResource(R.string.currency_value, currency, value),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = soraFont,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(spacing.dp))
        }
    }
}