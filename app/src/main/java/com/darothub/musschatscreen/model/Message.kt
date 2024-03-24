package com.darothub.musschatscreen.model

import java.time.Instant

data class Message (
    val sender: Sender,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)