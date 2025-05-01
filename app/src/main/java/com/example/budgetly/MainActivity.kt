package com.example.budgetly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetly.ui.BudgetlyApp
import com.example.budgetly.ui.theme.BudgetlyTheme
import com.example.budgetly.ui.currencyViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currencyViewModel: currencyViewModel = viewModel()
            BudgetlyTheme {
                Surface (
                    modifier = Modifier.background(Color(0xFFF5F7FA))
                ) {
                    BudgetlyApp(currencyViewModel)
                }
            }
        }
    }
}
