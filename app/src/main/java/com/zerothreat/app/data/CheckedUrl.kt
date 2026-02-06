package com.zerothreat.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checked_urls")
data class CheckedUrl(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val threatLevel: String, // "SAFE", "SUSPICIOUS", "PHISHING"
    val source: String, // "Manual Check", "SMS", "Browser", "Notification"
    val timestamp: Long = System.currentTimeMillis(),
    val reason: String = ""
)
