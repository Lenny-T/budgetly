package com.example.budgetly.data

import androidx.compose.ui.graphics.painter.Painter

data class currencies(
    val currency: String,
    val exchangeRate: Double,
    val currencyLogo: Painter,
)
