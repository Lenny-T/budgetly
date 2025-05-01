package com.example.budgetly.ui



import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetly.R
import com.example.budgetly.data.BottomNavigationItem
import com.example.budgetly.data.appScreens

@Composable
fun BudgetlyApp(currencyViewModel: currencyViewModel) {
    // NAVIGATION CONTROLLER
    val navController: NavHostController = rememberNavController()
    // LIST OF MENU ITEMS TO DISPLAY
    val menuList = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.dashboard),
            selectedIcon = painterResource(
                id = R.drawable.baseline_home_24,
            ),
            unselectedIcon = painterResource(id = R.drawable.outline_home_24)
        ),
        BottomNavigationItem(
            title = stringResource(R.string.transactions),
            selectedIcon = painterResource(id = R.drawable.baseline_receipt_long_24),
            unselectedIcon = painterResource(id = R.drawable.outline_receipt_long_24)
        ),
        BottomNavigationItem(
            title = stringResource(R.string.settings),
            selectedIcon = painterResource(id = R.drawable.baseline_settings_24),
            unselectedIcon = painterResource(id = R.drawable.outline_settings_24)
        )

    )

    // USED TO CHECK WHAT MENU ITEM WAS JUST CLICKED
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                ){
                // LOOP THROUGH ALL THE VALUES MENU ITEMS AND DISPLAY THEM IN A BOTTOM NAV
                menuList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        // REMOVED THE BACKGROUND INDICATOR WHEN A MENU ITEM IS CLICKED
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White
                        ),
                        onClick = {
                            selectedIndex = index
                            navController.navigate(route = item.title) },
                        label = {
                                Text(
                                    text = item.title,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = soraFont,
                                        fontWeight = FontWeight.Black
                                    ),
                                    color = if (index == selectedIndex){
                                        Color(0xFF21C277)
                                    } else Color(0xFF9E9FA3),
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                        },
                        icon = {
                            Icon(
                                painter = if (index == selectedIndex){
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                tint = if (index == selectedIndex){
                                    Color(0xFF21C277)
                                } else Color(0xFF9E9FA3),
                                contentDescription = item.title,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(appScreens.Settings.name) { SettingsScreen(currencyViewModel) }
            composable(appScreens.Dashboard.name) { DashboardScreen(currencyViewModel) }
            composable(appScreens.Transactions.name) { TransactionsPage(currencyViewModel) }
        }
    }

}