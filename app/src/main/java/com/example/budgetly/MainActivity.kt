package com.example.budgetly

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
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
        // SEND A THE NOTIFICATIONS
        scheduleDailyNotification()
    }

    // NOTIFICATIONS AT A SPECIFIC TIME
    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18) // SEND NOTIFICATION TO 6:30PM
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
        }

        // IF THE TIME HAS PASSED SEND REQUEST TO THE NEXT DAY
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}
