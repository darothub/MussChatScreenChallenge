package com.darothub.musschatscreen.data.entities

import java.time.Instant

data class Message (
    val sender: String,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)