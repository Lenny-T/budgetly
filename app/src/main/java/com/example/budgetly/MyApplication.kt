package com.example.budgetly

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication:Application() {

    // INITIALISED THE NOTIFICATION CHANNEL
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    // CREATE THE THE NOTIFICATION CHANNEL
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notification_channel",
                "Track Transactions",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for daily notifications"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}