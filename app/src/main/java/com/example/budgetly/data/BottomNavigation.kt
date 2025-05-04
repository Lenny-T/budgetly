package com.example.budgetly.data

import androidx.compose.ui.graphics.painter.Painter

// DATA CLASS FROM A MENU ITEM
data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter
)
